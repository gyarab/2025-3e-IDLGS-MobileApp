package cz.idlgs.mobile.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.idlgs.mobile.BuildConfig
import cz.idlgs.mobile.R
import cz.idlgs.mobile.nav.UserNavGraph
import cz.idlgs.mobile.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination<UserNavGraph>
@Composable
fun SettingsScreen(
	navigator: DestinationsNavigator,
	viewModel: SettingsViewModel = hiltViewModel(),
) {
	val scrollState = rememberScrollState()
	var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
	var showEditDialog by rememberSaveable { mutableStateOf(false) }
	val context = LocalContext.current

	if (showLogoutDialog) {
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
	}
	if (showEditDialog) {
		EditNameDialog(
			initialName = viewModel.name,
			initialSurname = viewModel.surname,
			onDismiss = { showEditDialog = false },
			onConfirm = { newName, newSurname ->
				viewModel.updateDetails(newName, newSurname)
				showEditDialog = false
			}
		)
	}

	Scaffold(
		topBar = {
			TopAppBar(title = { Text(stringResource(R.string.settings)) })
		}
	) { paddingValues ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues)
				.verticalScroll(scrollState)
		) {
			Text(
				text = "Account",
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier.padding(16.dp)
			)

			ListItem(
				headlineContent = {
					Text("${viewModel.name} ${viewModel.surname}")
				},
				supportingContent = { Text(viewModel.email) },
				leadingContent = {
					Icon(Icons.Default.Person, contentDescription = null)
				},
				trailingContent = {
					Icon(Icons.Default.ChevronRight, contentDescription = null)
				},
				modifier = Modifier.clickable { showEditDialog = true },
			)

			HorizontalDivider(Modifier.padding(horizontal = 16.dp))

			Text(
				text = stringResource(R.string.language),
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.primary,
				modifier = Modifier.padding(16.dp)
			)
			ListItem(
				headlineContent = { Text(stringResource(R.string.change_language)) },
				leadingContent = {
					Icon(Icons.Default.Language, contentDescription = null)
				},
				modifier = Modifier.clickable {
					openAppLanguages(context)
				}
			)

			HorizontalDivider(Modifier.padding(horizontal = 16.dp))

			ListItem(
				headlineContent = {
					Text(
						text = stringResource(R.string.log_out),
						color = MaterialTheme.colorScheme.error
					)
				},
				leadingContent = {
					Icon(
						Icons.AutoMirrored.Default.ExitToApp,
						null,
						tint = MaterialTheme.colorScheme.error
					)
				},
				modifier = Modifier.clickable { showLogoutDialog = true }
			)
		}
	}
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.BottomCenter,
	) {
		Text(
			text = BuildConfig.VERSION_NAME,
			style = MaterialTheme.typography.labelMedium,
			modifier = Modifier.padding(12.dp),
		)
	}
}

private fun openAppLanguages(context: Context) {
	val intent =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
				data = Uri.fromParts("package", context.packageName, null)
			}
		else Intent(Settings.ACTION_LOCALE_SETTINGS)
	context.startActivity(intent)
}

@Composable
fun EditNameDialog(
	initialName: String,
	initialSurname: String,
	onDismiss: () -> Unit,
	onConfirm: (String, String) -> Unit
) {
	var name by remember {
		mutableStateOf(
			TextFieldValue(initialName, TextRange(0, initialName.length))
		)
	}
	var surname by remember { mutableStateOf(TextFieldValue(initialSurname)) }
	val focusRequester = remember { FocusRequester() }

	AlertDialog(
		onDismissRequest = onDismiss,
		title = { Text("Edit Details") },
		text = {
			Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
				OutlinedTextField(
					value = name,
					onValueChange = { name = it },
					label = { Text("First Name") },
					singleLine = true,
					modifier = Modifier.focusRequester(focusRequester),
					keyboardOptions = KeyboardOptions(
						capitalization = KeyboardCapitalization.Sentences
					)
				)
				OutlinedTextField(
					value = surname,
					onValueChange = { surname = it },
					label = { Text("Last Name") },
					singleLine = true,
				)
			}
		},
		confirmButton = {
			TextButton(onClick = { onConfirm(name.text, surname.text) }) {
				Text(stringResource(R.string.save))
			}
		},
		dismissButton = {
			TextButton(onClick = onDismiss) {
				Text(stringResource(R.string.cancel))
			}
		}
	)
	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}
}
