package cz.idlgs.mobile.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.idlgs.mobile.R
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.UiUtils
import cz.idlgs.mobile.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(
	viewModel: AuthViewModel,
	onNavigateBack: () -> Unit
) {
	val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
	val focusRequester = remember { FocusRequester() }
	val msgInvalidEmail = stringResource(R.string.invalid_email_format)
	val msgEmailNotFound = stringResource(R.string.email_not_found)

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}

	val Header = @Composable {
		Text(
			text = stringResource(R.string.forgot_password),
			style = MaterialTheme.typography.headlineMedium
		)
		Text(
			text = "Enter your email to receive a reset link.",
			style = MaterialTheme.typography.bodyMedium,
			modifier = Modifier.padding(vertical = 16.dp)
		)
	}
	val Form = @Composable {
		OutlinedTextField(
			value = viewModel.email,
			onValueChange = viewModel::onEmailChange,
			label = { Text(stringResource(R.string.email)) },
			isError = viewModel.emailError != null,
			supportingText = {
				viewModel.emailError?.let { Text(it) }
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
				onGo = { viewModel.performForgotPassword() }
			)
		)

		Spacer(modifier = Modifier.height(8.dp))

		Button(
			onClick = { viewModel.performForgotPassword() },
			enabled = !viewModel.isLoading && viewModel.email.text.isNotEmpty(),
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			if (viewModel.isLoading)
				CircularProgressIndicator(
					modifier = Modifier.size(24.dp),
					color = MaterialTheme.colorScheme.onPrimary
				)
			else Text(stringResource(R.string.send_reset_link))
		}
		TextButton(
			onClick = onNavigateBack,
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			Text(stringResource(R.string.back_to_login))
		}
	}

	UiUtils.SplitIfLandscape(
		isLandscape = isLandscape,
		header = Header,
		form = Form
	)
}
@Preview(showBackground = true)
@Composable
fun ForgotScreenPreview() {
	IDLGSTheme {
		ForgotPasswordScreen(
			viewModel = viewModel(),
			onNavigateBack = {},
		)
	}
}
