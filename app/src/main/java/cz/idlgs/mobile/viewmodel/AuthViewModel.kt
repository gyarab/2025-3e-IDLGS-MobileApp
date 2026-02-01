package cz.idlgs.mobile.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.idlgs.mobile.R
import cz.idlgs.mobile.domain.repository.AuthRepository
import cz.idlgs.mobile.utils.UiEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
	private val authRepository: AuthRepository,
	private val uiEventManager: UiEventManager
) : ViewModel() {
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

	fun onEmailChange(newValue: TextFieldValue) {
		email = newValue.copy(text = newValue.text.trim())
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

	private val emailRegex = Patterns.EMAIL_ADDRESS.toRegex()
	private fun isEmailFormatValid(email: String) = email.matches(emailRegex)

	fun performLogin() {
		viewModelScope.launch {
			emailError = null
			passwordError = null

			if (!isEmailFormatValid(email.text)) {
				emailError = "Invalid email format"
				return@launch
			}
			if (password.isEmpty()) {
				passwordError = "Password cannot be empty"
				return@launch
			}

			isLoading = true
			delay(1500)

			val result = authRepository.login(email.text, password)
			if (result.isSuccess) uiEventManager.showToast("Logged in")
			else uiEventManager.showSnackbar("Login failed")

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
			uiEventManager.showSnackbar(R.string.not_yet_implemented)
			isLoading = false
		}
	}
}
