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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.idlgs.mobile.R
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.UiUtils
import cz.idlgs.mobile.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
	viewModel: AuthViewModel,
	onForgotPasswordClick: () -> Unit
) {
	val focusRequester = remember { FocusRequester() }
	val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}

	val Header = @Composable {
		Text(
			text = stringResource(R.string.login),
			style = MaterialTheme.typography.headlineMedium
		)
	}
	val Form = @Composable {
		OutlinedTextField(
			value = viewModel.email,
			onValueChange = viewModel::onEmailChange,
			label = { Text(stringResource(R.string.email)) },
			isError = viewModel.emailError != null,
			supportingText = { viewModel.emailError?.let { Text(it) } },
			modifier = Modifier
				.fillMaxWidth()
				.focusRequester(focusRequester),
			singleLine = true,
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Email,
				imeAction = ImeAction.Next
			),
		)
		Spacer(modifier = Modifier.height(8.dp))

		OutlinedTextField(
			value = viewModel.password,
			onValueChange = viewModel::onPasswordChange,
			label = { Text(stringResource(R.string.password)) },
			visualTransformation = PasswordVisualTransformation(),
			isError = viewModel.passwordError != null,
			supportingText = { viewModel.passwordError?.let { Text(it) } },
			modifier = Modifier.fillMaxWidth(),
			singleLine = true,
			keyboardOptions = KeyboardOptions(
				keyboardType = KeyboardType.Password,
				imeAction = ImeAction.Go
			),
			keyboardActions = KeyboardActions(
				onGo = { viewModel.performLogin() }
			)
		)
		Spacer(modifier = Modifier.height(8.dp))

		Button(
			onClick = viewModel::performLogin,
			enabled = !viewModel.isLoading && viewModel.email.text.isNotEmpty(),
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			if (viewModel.isLoading) CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				color = MaterialTheme.colorScheme.onPrimary
			)
			else Text(stringResource(R.string.log_in))
		}

		TextButton(
			onClick = onForgotPasswordClick,
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			Text(stringResource(R.string.forgot_password))
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
fun LoginScreenPreview() {
	IDLGSTheme {
		LoginScreen(
			viewModel = viewModel(),
			onForgotPasswordClick = {}
		)
	}
}
