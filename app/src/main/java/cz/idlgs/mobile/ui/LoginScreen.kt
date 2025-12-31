package cz.idlgs.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.idlgs.mobile.R
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.AuthUtils
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
				// e.g., if (!AuthUtils.isEmailFormatValid(email)) ...
			}
		}
		OutlinedTextField(
			value = email,
			onValueChange = {
				email = it
				scope.launch {
					emailError = null
					if (email.contains(AuthUtils.emailRegex) && !AuthUtils.isEmailFormatValid(email)) {
						emailError = "Invalid email format"
					}
				}
//				emailError = null
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
					if (!AuthUtils.isEmailFormatValid(email)) {
						emailError = "Invalid email format"
						return@launch
					}

					isLoading = true
					val exists = AuthUtils.doesEmailExist(email)
					isLoading = false

					if (exists) onLoginClick(email, password)
					else emailError = "Email not found"
				}
			},
			enabled = !isLoading && email.contains(AuthUtils.emailRegex),
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
