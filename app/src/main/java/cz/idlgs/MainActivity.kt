package cz.idlgs

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.idlgs.ui.LoginScreen
import cz.idlgs.ui.theme.IDLGSTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			IDLGSTheme {
				IDLGSApp()
			}
		}
	}
}
@PreviewScreenSizes
@Composable
fun IDLGSApp() {
	var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
	val context = LocalContext.current

	NavigationSuiteScaffold(
		navigationSuiteItems = {
			AppDestinations.entries.forEach {
				item(
					icon = {
						Icon(
							it.icon,
							contentDescription = stringResource(it.labelRes)
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
				LoginScreen(
					onLoginClick = { _, _ ->
						Toast.makeText(
							context,
							context.getString(R.string.not_yet_implemented), 
							Toast.LENGTH_SHORT
						).show()
					},
					onForgotPasswordClick = {}
				)
			}
			else -> {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					Box(
						modifier = Modifier
							.padding(innerPadding)
							.fillMaxSize(),
						contentAlignment = Alignment.TopCenter
					) {
						Greeting(
							name = "User",
							modifier = Modifier.padding(top = 40.dp),
							fontSize = 20,
							isBold = true
						)
					}
				}
			}
		}
	}
}

enum class AppDestinations(
	@StringRes val labelRes: Int,
	val icon: ImageVector,
) {
	HOME(R.string.home, Icons.Default.Home),
	//	FAVORITES(R.string.favorites, Icons.Default.Favorite),
	PROFILE(R.string.profile, Icons.Default.AccountBox),
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, isBold: Boolean = false, fontSize: Int = 16) {
	Text(
		text = "Hello $name!",
		modifier = modifier,
		fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
		fontSize = fontSize.sp,
		style = cz.idlgs.ui.theme.Typography.bodyLarge
	)
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	IDLGSTheme {
		Greeting("Android", isBold = true)
	}
}
