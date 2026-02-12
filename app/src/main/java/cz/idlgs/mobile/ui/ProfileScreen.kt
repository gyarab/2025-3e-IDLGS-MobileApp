package cz.idlgs.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import cz.idlgs.mobile.R
import cz.idlgs.mobile.nav.UserNavGraph
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.viewmodel.ProfileViewModel

@Destination<UserNavGraph>
@Composable
fun ProfileScreen(
	navigator: DestinationsNavigator,
	viewModel: ProfileViewModel = hiltViewModel(),
) {
	val scrollState = rememberScrollState()
	var showLogoutDialog by rememberSaveable { mutableStateOf(false) }

	if (showLogoutDialog)
		AlertDialog(
			onDismissRequest = { showLogoutDialog = false },
			title = { Text(text = stringResource(R.string.log_out)) },
			text = { Text(text = "Are you sure you want to log out?") },
			confirmButton = {
				TextButton(
					onClick = {
						showLogoutDialog = false
						viewModel.logout(navigator)
					}
				) {
					Text(stringResource(android.R.string.ok))
				}
			},
			dismissButton = {
				TextButton(onClick = { showLogoutDialog = false }) {
					Text(stringResource(android.R.string.cancel))
				}
			}
		)

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
			.verticalScroll(scrollState),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Text(
			text = stringResource(R.string.you_re_logged_in),
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			modifier = Modifier.padding(bottom = 8.dp)
		)

		Column(Modifier.fillMaxWidth()) {
			Column {
				Text(
					text = stringResource(R.string.name_label),
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp
				)
				Text(
					text = viewModel.name,
					fontSize = 18.sp
				)
				Spacer(modifier = Modifier.width(8.dp))
				Text(
					text = stringResource(R.string.email_label),
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp
				)
				Text(
					text = viewModel.email,
					fontSize = 18.sp
				)
			}
		}

		Spacer(modifier = Modifier.height(48.dp))

		Box(
			modifier = Modifier
				.size(160.dp)
				.background(MaterialTheme.colorScheme.primary),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = viewModel.name.firstOrNull().toString(),
				fontSize = 60.sp,
				color = MaterialTheme.colorScheme.onPrimary
			)
		}

		Spacer(modifier = Modifier.height(60.dp))

		Button(
			onClick = { showLogoutDialog = true },
			shape = MaterialTheme.shapes.small,
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.error,
				contentColor = MaterialTheme.colorScheme.onError
			),
			modifier = Modifier
				.width(200.dp)
				.height(48.dp)
		) {
			Text(
				text = stringResource(R.string.log_out),
				fontWeight = FontWeight.Bold,
				fontSize = 18.sp
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
	IDLGSTheme {
		ProfileScreen(EmptyDestinationsNavigator, viewModel())
	}
}
