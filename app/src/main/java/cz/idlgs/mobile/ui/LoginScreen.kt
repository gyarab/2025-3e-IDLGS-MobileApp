package cz.idlgs.mobile.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.idlgs.mobile.R
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.AuthUtils
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
	onLoginClick: (String, String) -> Unit,
	onForgotPasswordClick: () -> Unit
) {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var emailError by remember { mutableStateOf<String?>(null) }
	var isLoading by remember { mutableStateOf(false) }

	val scrollState = rememberScrollState()
	val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
	val scope = rememberCoroutineScope()
	val msgInvalidEmail = stringResource(R.string.invalid_email_format)
	val msgEmailNotFound = stringResource(R.string.email_not_found)

	val performLogin: () -> Unit = {
		scope.launch {
			emailError = null
			if (!AuthUtils.isEmailFormatValid(email)) {
				emailError = msgInvalidEmail
				return@launch
			}
			isLoading = true
			val exists = AuthUtils.doesEmailExist(email)
			isLoading = false

			if (exists) onLoginClick(email, password)
			else emailError = msgEmailNotFound
		}
	}
	val Header = @Composable {
		Text(
			text = stringResource(R.string.login),
			style = MaterialTheme.typography.headlineMedium
		)
	}
	val Form = @Composable {
		LoginInputs(
			email = email,
			password = password,
			emailError = emailError,
			isLoading = isLoading,
			onEmailChange = { email = it; emailError = null },
			onPasswordChange = { password = it },
			onLogin = performLogin,
			onForgotPassword = onForgotPasswordClick
		)
	}

	if (isLandscape)
		Row(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Column(
				Modifier.weight(1f),
				horizontalAlignment = Alignment.CenterHorizontally
			) { Header() }
			Column(
				Modifier
					.weight(1f)
					.verticalScroll(scrollState),
				horizontalAlignment = Alignment.CenterHorizontally
			) { Form() }
		}
	else
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
				.verticalScroll(scrollState),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Header()
			Spacer(Modifier.height(32.dp))
			Form()
		}
}
@Composable
private fun LoginInputs(
	email: String,
	password: String,
	emailError: String?,
	isLoading: Boolean,
	onEmailChange: (String) -> Unit,
	onPasswordChange: (String) -> Unit,
	onLogin: () -> Unit,
	onForgotPassword: () -> Unit
) {
	OutlinedTextField(
		value = email,
		onValueChange = onEmailChange,
		label = { Text(stringResource(R.string.email)) },
		isError = emailError != null,
		supportingText = { emailError?.let { Text(it) } },
		modifier = Modifier.fillMaxWidth(),
		singleLine = true
	)
	Spacer(modifier = Modifier.height(8.dp))

	OutlinedTextField(
		value = password,
		onValueChange = onPasswordChange,
		label = { Text(stringResource(R.string.password)) },
		visualTransformation = PasswordVisualTransformation(),
		modifier = Modifier.fillMaxWidth(),
		singleLine = true
	)
	Spacer(modifier = Modifier.height(20.dp))

	Button(
		onClick = onLogin,
		enabled = !isLoading && email.isNotEmpty(),
		modifier = Modifier.fillMaxWidth(.9f)
	) {
		if (isLoading)
			CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				color = MaterialTheme.colorScheme.onPrimary
			)
		else Text(stringResource(R.string.log_in))
	}

	TextButton(onClick = onForgotPassword) {
		Text(stringResource(R.string.forgot_password))
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
@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun LoginScreenLandscapePreview() {
	IDLGSTheme {
		LoginScreen(
			onLoginClick = { _, _ -> },
			onForgotPasswordClick = {}
		)
	}
}
