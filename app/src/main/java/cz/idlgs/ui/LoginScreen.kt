package cz.idlgs.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.idlgs.R
import cz.idlgs.ui.theme.IDLGSTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
	onLoginClick: (String, String) -> Unit,
	onForgotPasswordClick: () -> Unit
) {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	// State for validation
	var emailError by remember { mutableStateOf<String?>(null) }
	var isLoading by remember { mutableStateOf(false) }
	val scope = rememberCoroutineScope()
	// 1. Regex Validation
	fun isEmailFormatValid(email: String): Boolean {
		val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
		return email.matches(emailRegex)
	}
	// 2. Mock API Check to see if email exists
	suspend fun doesEmailExist(email: String): Boolean {
		isLoading = true
		delay(1500) // Simulate network request time
		// Mock logic: In a real app, you would call your backend here
		isLoading = false
		return email == "user@example.com"
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = stringResource(R.string.login),
			style = MaterialTheme.typography.headlineMedium
		)
		Spacer(modifier = Modifier.height(32.dp))

		LaunchedEffect(email) {
			if (email.isNotEmpty()) {
				delay(500) // Wait for user to stop typing for 500ms
				// Perform validation here automatically
				// e.g., if (!isEmailFormatValid(email)) ...
			}
		}
		OutlinedTextField(
			value = email,
			onValueChange = {
				email = it
				scope.launch {
					emailError = null
					val temp = email.split("@")
					if (!email.contains(Regex("@\\w+\\.\\w+")) || temp[1].length < 4) return@launch
					// Step 1: Check Regex
					if (!isEmailFormatValid(email)) {
						emailError = "Invalid email format"
						return@launch
					}
					// Step 2: Check Existence (Async)
					val exists = doesEmailExist(email)

					if (exists) onLoginClick(email, password)
					else emailError = "Email not found (Try: user@example.com)"
				}
//				emailError = null // Clear error when user types
			},
			label = { Text(stringResource(R.string.email)) },
			isError = emailError != null,
			supportingText = {
				emailError?.let { Text(it) }
			},
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)
		Spacer(modifier = Modifier.height(12.dp))

		OutlinedTextField(
			value = password,
			onValueChange = { password = it },
			label = { Text(stringResource(R.string.password)) },
			visualTransformation = PasswordVisualTransformation(),
			modifier = Modifier.fillMaxWidth(),
			singleLine = true
		)
		Spacer(modifier = Modifier.height(20.dp))

		Button(
			onClick = {
				scope.launch {
					emailError = null
					if (!email.contains(Regex("@\\w+\\.\\w+"))) {
						emailError = "Required"
						return@launch
					}

					// Step 1: Check Regex
					if (!isEmailFormatValid(email)) {
						emailError = "Invalid email format"
						return@launch
					}
					// Step 2: Check Existence (Async)
//					val exists = doesEmailExist(email)

					if (true) onLoginClick(email, password)
					else emailError = "Email not found (Try: user@example.com)"
				}
			},
			enabled = !isLoading,
			modifier = Modifier.fillMaxWidth()
		) {
			if (isLoading) {
				CircularProgressIndicator(
					modifier = Modifier.size(24.dp),
					color = MaterialTheme.colorScheme.onPrimary
				)
			} else Text(stringResource(R.string.log_in))
		}

		TextButton(onClick = onForgotPasswordClick) {
			Text(stringResource(R.string.forgot_password))
		}
	}
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
	IDLGSTheme {
		LoginScreen(
			onLoginClick = { _, _ -> },
			onForgotPasswordClick = {}
		)
	}
}
