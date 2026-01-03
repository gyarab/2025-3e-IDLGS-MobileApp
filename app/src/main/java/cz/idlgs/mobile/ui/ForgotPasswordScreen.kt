package cz.idlgs.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.idlgs.mobile.R
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.AuthUtils
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
	email: TextFieldValue,
	onEmailChange: (TextFieldValue) -> Unit,
	onNavigateBack: () -> Unit,
	onSubmitClick: (String) -> Unit
) {
	var emailError by remember { mutableStateOf<String?>(null) }
	var isLoading by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()
	val focusRequester = remember { FocusRequester() }
	val msgInvalidEmail = stringResource(R.string.invalid_email_format)
	val msgEmailNotFound = stringResource(R.string.email_not_found)

	val performSubmit = {
		scope.launch {
			emailError = null
			if (!AuthUtils.isEmailFormatValid(email.text)) {
				emailError = msgInvalidEmail
				return@launch
			}
			isLoading = true
			val exists = AuthUtils.doesEmailExist(email.text)
			isLoading = false

			if (exists) onSubmitClick(email.text)
			else emailError = msgEmailNotFound
		}
	}

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
		if (email.text.isNotEmpty())
			onEmailChange(email.copy(selection = TextRange(email.text.length)))
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = stringResource(R.string.forgot_password),
			style = MaterialTheme.typography.headlineMedium
		)

		Text(
			text = "Enter your email to receive a reset link.",
			style = MaterialTheme.typography.bodyMedium,
			modifier = Modifier.padding(vertical = 16.dp)
		)

		OutlinedTextField(
			value = email,
			onValueChange = { 
				onEmailChange(it)
				emailError = null
			},
			label = { Text(stringResource(R.string.email)) },
			isError = emailError != null,
			supportingText = {
				emailError?.let { Text(it) }
			},
			modifier = Modifier
				.fillMaxWidth()
				.focusRequester(focusRequester),
			singleLine = true,
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Email,
				imeAction = ImeAction.Go
			),
			keyboardActions = KeyboardActions(
				onGo = { performSubmit() }
			)
		)

		Spacer(modifier = Modifier.height(24.dp))

		Button(
			onClick = { performSubmit() },
			enabled = !isLoading && email.text.isNotEmpty(),
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			if (isLoading)
				CircularProgressIndicator(
					modifier = Modifier.size(24.dp),
					color = MaterialTheme.colorScheme.onPrimary
				)
			else Text(stringResource(R.string.send_reset_link))
		}

		TextButton(onClick = onNavigateBack) {
			Text(stringResource(R.string.back_to_login))
		}
	}
}
@Preview(showBackground = true)
@Composable
fun ForgotScreenPreview() {
	IDLGSTheme {
		ForgotPasswordScreen(
			email = TextFieldValue("user@example.com"),
			onEmailChange = {},
			onNavigateBack = {},
			onSubmitClick = {}
		)
	}
}
