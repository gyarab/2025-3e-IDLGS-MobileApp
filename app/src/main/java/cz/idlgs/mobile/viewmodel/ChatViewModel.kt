package cz.idlgs.mobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import cz.idlgs.mobile.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.BufferedReader

data class ChatMessage(
	val role: Role,
	val content: String
)

@Suppress("EnumEntryName")
enum class Role {
	user, assistant, system, error,
}

data class OpenAIRequest(
	val model: String,
	val messages: List<ChatMessage>,
	val stream: Boolean
)

data class OpenAIResponse(
	val choices: List<Choice>? = null
)

data class Choice(
	val message: ChatMessage
)

class ChatViewModel() : ViewModel() {
	private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
	val messages = _messages.asStateFlow()

	private val _isLoading = MutableStateFlow(false)
	val isLoading = _isLoading.asStateFlow()

	private val client = OkHttpClient()
	private val gson = Gson()

	private val BASE_URL = BuildConfig.LM_STUDIO_URL
	private val USE_STREAMING = true

	private val MODEL_LIST = listOf(
//		"mistralai/ministral-3-14b-reasoning",
		"qwen/qwen3-vl-8b",
	)

	fun sendMessage(content: String) {
		if (content.isBlank()) return

		val userMessage = ChatMessage(Role.user, content)
		_messages.value += userMessage
		_isLoading.value = true

		viewModelScope.launch(Dispatchers.IO) {
			var lastError: Exception? = null
			_messages.value = _messages.value.filter { it.role != Role.error }

			for (model in MODEL_LIST) {
				Log.d("ChatViewModel", "Sending message to model $model")
				try {
					performRequest(model, USE_STREAMING)
					lastError = null
					break
				} catch (e: Exception) {
					lastError = e
					if (USE_STREAMING) {
						val lastMsg = _messages.value.lastOrNull()
						if (lastMsg != null && lastMsg.role == Role.assistant)
							_messages.value = _messages.value.dropLast(1)
					}
				}
			}
			lastError?.let {
				_messages.value += ChatMessage(
					Role.error,
					"Error:\nPlease try again later"
				)
				if (BuildConfig.DEBUG)
					Log.e("ChatViewModel", it.stackTraceToString())
			}
			_isLoading.value = false
		}
	}

	private fun performRequest(model: String, stream: Boolean) {
		val requestBody = OpenAIRequest(model = model, messages = _messages.value, stream = stream)
		val jsonBody = gson.toJson(requestBody)
		val mediaType = "application/json; charset=utf-8".toMediaType()
		val body = jsonBody.toRequestBody(mediaType)

		val request = Request.Builder()
			.url(BASE_URL)
			.addHeader("skip_zrok_interstitial", "1")
			.post(body)
			.build()

		client.newCall(request).execute().use { response ->
			if (!response.isSuccessful) {
				throw Exception("Server returned code ${response.code}")
			}

			if (stream) handleStreamingResponse(response)
			else handleBlockingResponse(response, model)
		}
	}

	private fun handleBlockingResponse(response: Response, model: String) {
		val responseData = response.body.string()
		val openAIResponse = gson.fromJson(responseData, OpenAIResponse::class.java)

		val assistantMessage = openAIResponse.choices?.firstOrNull()?.message
			?: ChatMessage(
				Role.error,
				"Empty response from AI" + if (BuildConfig.DEBUG) " for model $model" else ""
			)
		_messages.value += assistantMessage
	}

	private fun handleStreamingResponse(response: Response) {
		_messages.value += ChatMessage(Role.assistant, "")
		val messageIndex = _messages.value.lastIndex

		val source = response.body.byteStream()
		val reader = BufferedReader(source.reader())
		var line: String?

		while (reader.readLine().also { line = it } != null) {
			val currentLine = line ?: continue
			if (currentLine.startsWith("data: ")) {
				val data = currentLine.removePrefix("data: ").trim()
				if (data == "[DONE]") break

				try {
					val jsonObject = gson.fromJson(data, JsonObject::class.java)
					val choices = jsonObject.getAsJsonArray("choices")
					if (choices != null && choices.size() > 0) {
						val delta = choices.get(0).asJsonObject.get("delta").asJsonObject
						if (delta.has("content")) {
							val contentChunk = delta.get("content").asString
							if (contentChunk.isNotEmpty()) {
								val currentList = _messages.value.toMutableList()
								val currentMsg = currentList[messageIndex]
								currentList[messageIndex] =
									currentMsg.copy(content = currentMsg.content + contentChunk)
								_messages.value = currentList
							}
						}
					}
				} catch (e: Exception) {
					Log.e("ChatViewModel", "Parsing error", e)
				}
			}
		}
	}
}
