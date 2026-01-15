package cz.idlgs.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed class AuthUiEvent {
	data class ShowMessage(val message: String) : AuthUiEvent()
	data class ShowSnackbar(val message: String) : AuthUiEvent()
}

class AuthViewModel : ViewModel() {
	var email by mutableStateOf(TextFieldValue(""))
		private set
	var password by mutableStateOf("")
		private set
	var emailError by mutableStateOf<String?>(null)
		private set
	var passwordError by mutableStateOf<String?>(null)
		private set
	var isLoading by mutableStateOf(false)
		private set

	private val _uiEvent = MutableSharedFlow<AuthUiEvent>()
	val uiEvent = _uiEvent.asSharedFlow()

	fun onEmailChange(newValue: TextFieldValue) {
		email = newValue
		emailError = null
	}

	fun onPasswordChange(newValue: String) {
		password = newValue
		passwordError = null
	}

	fun selectEmail(vararg i: Int) {
		if (email.text.isNotEmpty())
			when (i.size) {
				0 -> email = email.copy(selection = TextRange(email.text.length))
				1 -> email = email.copy(selection = TextRange(i[0]))
				2 -> email = email.copy(selection = TextRange(i[0], i[1]))
			}
	}

	private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
	private fun isEmailFormatValid(email: String) = email.matches(emailRegex)

	fun performLogin() {
		viewModelScope.launch {
			emailError = null
			passwordError = null

			if (!isEmailFormatValid(email.text)) {
				emailError = "Invalid email format"
				return@launch
			}
			if (password.isBlank()) {
				passwordError = "Password cannot be empty"
				return@launch
			}

			isLoading = true
			delay(1000)
			_uiEvent.emit(AuthUiEvent.ShowMessage("Not implemented yet"))

			isLoading = false
		}
	}

	fun performForgotPassword() {
		viewModelScope.launch {
			emailError = null
			if (!isEmailFormatValid(email.text)) {
				emailError = "Invalid email format"
				return@launch
			}

			isLoading = true
			delay(1000)
			_uiEvent.emit(AuthUiEvent.ShowMessage("Not implemented yet"))
			isLoading = false
		}
	}
}
