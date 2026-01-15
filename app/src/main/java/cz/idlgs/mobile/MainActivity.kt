package cz.idlgs.mobile

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.idlgs.mobile.ui.*
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.ui.theme.Typography
import cz.idlgs.mobile.utils.Utils
import cz.idlgs.mobile.utils.Utils.showToast
import cz.idlgs.mobile.viewmodel.AuthUiEvent
import cz.idlgs.mobile.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			IDLGSTheme { IDLGSApp() }
		}
	}
}

@PreviewScreenSizes
@Composable
fun IDLGSApp() {
	var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
	var showForgotPassword by rememberSaveable { mutableStateOf(false) }
	var showChat by rememberSaveable { mutableStateOf(false) }
	val authViewModel: AuthViewModel = viewModel()

	val msgNotImplemented = stringResource(R.string.not_yet_implemented)
	val msgResetLinkSent = stringResource(R.string.reset_link_sent_to)

	val context = LocalContext.current
	val configuration = LocalConfiguration.current
	val snackbarHostState = remember { SnackbarHostState() }
	LaunchedEffect(authViewModel.uiEvent) {
		authViewModel.uiEvent.collect { event ->
			when (event) {
				is AuthUiEvent.ShowMessage -> context.showToast(event.message)
				is AuthUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
			}
		}
	}

	val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
	val notLargeScreen = configuration.smallestScreenWidthDp < 600
	val layoutType =
		when {
			(notLargeScreen && isLandscape) -> NavigationSuiteType.NavigationRail
			notLargeScreen -> NavigationSuiteType.ShortNavigationBarMedium
			else -> Utils.adaptiveNavSuiteType()
		}
	if (showChat) ChatDialog(onDismiss = { showChat = false })
	if (BuildConfig.DEBUG) context.showToast("Version ${BuildConfig.VERSION_NAME}")


	NavigationSuiteScaffold(
		modifier = Modifier.windowInsetsPadding(WindowInsets.ime),
		layoutType = layoutType,
		navigationSuiteItems = {
			AppDestinations.entries.forEach {
				item(
					icon = {
						Icon(
							it.icon,
							stringResource(it.labelRes)
						)
					},
					label = { Text(stringResource(it.labelRes)) },
					onClick = { currentDestination = it },
					selected = it == currentDestination,
				)
			}
		}
	) {
		when (currentDestination) {
			AppDestinations.PROFILE -> {
				Box(
					modifier = Modifier.fillMaxSize()
				) {
					AnimatedContent(
						targetState = showForgotPassword,
						transitionSpec = {
							if (targetState)
								(slideInHorizontally { width -> width } + fadeIn())
									.togetherWith(slideOutHorizontally { width -> -width } + fadeOut())
							else
								(slideInHorizontally { width -> -width } + fadeIn())
									.togetherWith(slideOutHorizontally { width -> width } + fadeOut())
						}, label = "auth_transition"
					) { isForgotPassword ->
						if (isForgotPassword)
							ForgotPasswordScreen(
								viewModel = authViewModel,
								onNavigateBack = { showForgotPassword = false }
							)
						else
							LoginScreen(
								viewModel = authViewModel,
								onForgotPasswordClick = { showForgotPassword = true }
							)
					}
				}
			}
			else -> Scaffold(
				modifier = Modifier.fillMaxSize()
			) { innerPadding ->
				Box(
					modifier = Modifier
						.padding(innerPadding)
						.fillMaxSize(),
					contentAlignment = Alignment.TopCenter
				) {
					HomeScreen { showChat = true }
				}
			}
		}
	}
}

enum class AppDestinations(
	@param:StringRes val labelRes: Int,
	val icon: ImageVector,
) {
	HOME(R.string.home, Icons.Default.Home),
	//	FAVORITES(R.string.favorites, Icons.Default.Favorite),
	PROFILE(R.string.profile, Icons.Default.AccountBox),
}

@Composable
fun Greeting(
	name: String,
	modifier: Modifier = Modifier,
	isBold: Boolean = false,
	fontSize: Int = 16
) {
	Text(
		text = "Hello $name!",
		modifier = modifier,
		fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
		fontSize = fontSize.sp,
		style = Typography.bodyLarge
	)
}
