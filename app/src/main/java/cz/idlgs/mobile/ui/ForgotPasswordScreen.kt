package cz.idlgs.mobile.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import cz.idlgs.mobile.R
import cz.idlgs.mobile.nav.AuthNavGraph
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.UiUtils
import cz.idlgs.mobile.viewmodel.AuthViewModel

@Destination<AuthNavGraph>
@Composable
fun ForgotPasswordScreen(
	navigator: DestinationsNavigator,
	viewModel: AuthViewModel = hiltViewModel(),
) {
	val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}

	val Header = @Composable {
		Text(
			text = stringResource(R.string.forgot_password),
			style = MaterialTheme.typography.headlineLarge
		)
		Text(
			text = "Enter your email to receive a reset link.",
			style = MaterialTheme.typography.bodyLarge,
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
			leadingIcon = {
				Icon(
					Icons.Default.Email,
					contentDescription = null
				)
			},
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
			LoadingCircle(viewModel.isLoading, R.string.send_reset_link)
		}
		TextButton(
			onClick = { navigator.navigateUp() },
			modifier = Modifier.fillMaxWidth(.9f)
		) {
			Text(
				stringResource(R.string.back_to_login),
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
@Preview(showBackground = true)
@Composable
fun ForgotScreenPreview() {
	IDLGSTheme {
		ForgotPasswordScreen(
			navigator = EmptyDestinationsNavigator,
			viewModel = viewModel(),
		)
	}
}
