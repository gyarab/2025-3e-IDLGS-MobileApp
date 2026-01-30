package cz.idlgs.mobile

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.ForgotPasswordScreenDestination
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.TypedDestinationSpec
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator
import cz.idlgs.mobile.nav.guestDestinations
import cz.idlgs.mobile.nav.userDestinations
import cz.idlgs.mobile.repository.AuthRepository
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.ui.theme.Typography
import cz.idlgs.mobile.utils.UiUtils
import cz.idlgs.mobile.utils.Utils.showToast
import cz.idlgs.mobile.viewmodel.*

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
	val authRepository = AuthRepository()
	val authViewModel: AuthViewModel = viewModel(factory = AuthVMFactory(authRepository))
	val profileViewModel: ProfileViewModel = viewModel(factory = ProfileVMFactory(authRepository))
	val isLoggedIn by authViewModel.isLoggedIn.collectAsStateWithLifecycle()
	val currentDestinations = if (isLoggedIn) userDestinations else guestDestinations

	val context = LocalContext.current
	val configuration = LocalConfiguration.current
	val snackbarHostState = remember { SnackbarHostState() }

	val navController = rememberNavController()
	val navigator = navController.rememberDestinationsNavigator()
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val destination = navBackStackEntry?.destination
	fun isSameDestination(it: Direction) = destination?.route == it.route

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
			else -> UiUtils.adaptiveNavSuiteType()
		}

	NavigationSuiteScaffold(
		modifier = Modifier
			.fillMaxSize()
			.windowInsetsPadding(WindowInsets.safeDrawing)
			.consumeWindowInsets(WindowInsets.safeDrawing),
		layoutType = layoutType,
		navigationSuiteItems = {
			currentDestinations.forEach {
				item(
					icon = {
						Icon(
							it.icon,
							stringResource(it.labelRes)
						)
					},
					label = { Text(stringResource(it.labelRes)) },
					selected = isSameDestination(it.direction),
					onClick = {
						if (isSameDestination(it.direction)) return@item
						navigator.navigate(it.direction) {
							popUpTo(NavGraphs.root) { inclusive = true }
							launchSingleTop = true
						}
					},
				)
			}
		},
	) {
		DestinationsNavHost(
			navController = navController,
			navGraph = NavGraphs.root,
			dependenciesContainerBuilder = {
				when {
					isIn(LoginScreenDestination, ForgotPasswordScreenDestination) ->
						dependency(authViewModel)
					isIn(ProfileScreenDestination) ->
						dependency(profileViewModel)
				}
			},
		)
	}
}

private fun DependenciesContainerBuilder<*>.isIn(vararg destinations: TypedDestinationSpec<*>) =
	destination in destinations

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
