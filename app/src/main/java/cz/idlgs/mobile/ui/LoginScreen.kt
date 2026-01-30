package cz.idlgs.mobile.ui

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.ForgotPasswordScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import cz.idlgs.mobile.R
import cz.idlgs.mobile.nav.AuthNavGraph
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.UiUtils
import cz.idlgs.mobile.viewmodel.AuthViewModel

@Destination<AuthNavGraph>(start = true)
@Composable
fun LoginScreen(
	navigator: DestinationsNavigator,
	viewModel: AuthViewModel,
) {
	val focusRequester = remember { FocusRequester() }
	val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
	val showPassword = rememberSaveable { mutableStateOf(false) }

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}

	val Header = @Composable {
		Text(
			text = stringResource(R.string.login),
			style = MaterialTheme.typography.headlineLarge
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
			visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
			isError = viewModel.passwordError != null,
			supportingText = { viewModel.passwordError?.let { Text(it) } },
			modifier = Modifier.fillMaxWidth(),
			singleLine = true,
			trailingIcon = {
				IconButton(onClick = { showPassword.value = !showPassword.value }) {
					Icon(
						if (showPassword.value) Icons.Default.Visibility else Icons.Default.VisibilityOff,
						contentDescription = null
					)
				}
			},
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
			LoadingCircle(viewModel.isLoading, R.string.login)
		}

		TextButton(
			onClick = { navigator.navigate(ForgotPasswordScreenDestination) },
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			Text(
				stringResource(R.string.forgot_password),
				style = LocalTextStyle.current.copy(fontSize = 16.sp)
			)
		}
	}

	UiUtils.SplitIfLandscape(
		isLandscape = isLandscape,
		header = Header,
		form = Form
	)
}

@Composable
fun LoadingCircle(isLoading: Boolean, @StringRes textId: Int) {
	if (isLoading) CircularProgressIndicator(
		modifier = Modifier.size(32.dp),
		color = MaterialTheme.colorScheme.primary
	)
	else Text(
		stringResource(textId),
		style = LocalTextStyle.current.copy(fontSize = 16.sp)
	)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
	IDLGSTheme {
		LoginScreen(EmptyDestinationsNavigator, viewModel())
	}
}
