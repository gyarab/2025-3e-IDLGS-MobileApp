package cz.idlgs.mobile.data.repository

import com.google.gson.Gson
import cz.idlgs.mobile.BuildConfig
import cz.idlgs.mobile.data.remote.dto.ChatMessage
import cz.idlgs.mobile.data.remote.dto.OpenAIRequest
import cz.idlgs.mobile.domain.repository.ChatRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class LMServerRepositoryImpl : ChatRepository {
	override val client = OkHttpClient()
	override val gson = Gson()

	override fun performRequest(
		messages: List<ChatMessage>, model: String, stream: Boolean, onChunkReceived: (String) -> Unit
	) {
		val requestBody = OpenAIRequest(model = model, messages = messages, stream = stream)
		val jsonBody = gson.toJson(requestBody)
		val mediaType = "application/json; charset=utf-8".toMediaType()
		val body = jsonBody.toRequestBody(mediaType)

		val request = Request.Builder()
			.url(BuildConfig.LM_STUDIO_URL)
			.addHeader("Content-Type", "application/json")
			.addHeader("skip_zrok_interstitial", "1")
			.post(body)
			.build()

		client.newCall(request).execute().use { response ->
			if (!response.isSuccessful)
				throw Exception("Server returned code ${response.code}")

			if (stream) handleStreamingResponse(response) { onChunkReceived(it) }
			else handleBlockingResponse(response) { onChunkReceived(it) }
		}
	}
}