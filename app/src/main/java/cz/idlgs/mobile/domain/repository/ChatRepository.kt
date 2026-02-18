package cz.idlgs.mobile.domain.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import cz.idlgs.mobile.data.remote.dto.ChatMessage
import cz.idlgs.mobile.data.remote.dto.OpenAIResponse
import cz.idlgs.mobile.data.remote.dto.Role
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.BufferedReader

interface ChatRepository {
	val client: OkHttpClient
	val gson: Gson

	fun performRequest(
		messages: List<ChatMessage>, model: String, stream: Boolean, onChunkReceived: (String) -> Unit
	)

	fun handleBlockingResponse(response: Response): ChatMessage {
		val responseData = response.body.string()
		val openAIResponse = gson.fromJson(responseData, OpenAIResponse::class.java)

		val assistantMessage = openAIResponse.choices?.firstOrNull()?.message
			?: ChatMessage(Role.error, "Empty response from AI")
		return assistantMessage
	}

	fun handleStreamingResponse(
		response: Response, onChunkReceived: (String) -> Unit
	) {
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
							if (contentChunk.isNotEmpty())
								onChunkReceived(contentChunk)
						}
					}
				} catch (e: Exception) {

				}
			}
		}
	}
}