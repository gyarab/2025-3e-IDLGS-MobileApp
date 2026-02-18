package cz.idlgs.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.idlgs.mobile.data.remote.dto.ChatMessage
import cz.idlgs.mobile.data.remote.dto.Role
import cz.idlgs.mobile.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
	private val chatRepository: ChatRepository
) : ViewModel() {
	private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
	val messages = _messages.asStateFlow()

	private val _isLoading = MutableStateFlow(false)
	val isLoading = _isLoading.asStateFlow()

	private val USE_STREAMING = true
	private val MODEL_LIST = listOf(
		"moonshotai/kimi-k2-instruct-0905",
	)

	fun sendMessage(content: String) {
		if (content.isBlank()) return

		val userMessage = ChatMessage(Role.user, content.trim())
		_messages.value += userMessage
		_isLoading.value = true

		viewModelScope.launch(Dispatchers.IO) {
			var lastError: Exception? = null
			_messages.value = _messages.value.filter { it.role != Role.error }

			for (model in MODEL_LIST) {
				try {
					_messages.value += ChatMessage(Role.assistant, "")
					val msgIndex = _messages.value.lastIndex

					chatRepository.performRequest(_messages.value, model, !USE_STREAMING) {
						val list = _messages.value.toMutableList()
						list[msgIndex] = list[msgIndex].copy(content = list[msgIndex].content + it)
						_messages.value = list
					}
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
				_messages.value += ChatMessage(Role.error, "Error:\nPlease try again later")
			}
			_isLoading.value = false
		}
	}
}
