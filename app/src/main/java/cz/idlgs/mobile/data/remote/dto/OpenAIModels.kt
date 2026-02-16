package cz.idlgs.mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ChatMessage(
	val role: Role,
	val content: String
)

@Serializable
enum class Role {
	@SerialName("user") user,
	@SerialName("assistant") assistant,
	@SerialName("system") system,
	@SerialName("error") error,
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