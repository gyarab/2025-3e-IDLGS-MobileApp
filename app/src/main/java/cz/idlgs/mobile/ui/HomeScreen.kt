package cz.idlgs.mobile.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.ChatDialogDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import cz.idlgs.mobile.BuildConfig
import cz.idlgs.mobile.Greeting
import cz.idlgs.mobile.nav.HomeNavGraph
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.Utils.openLinkInBrowser

@Destination<HomeNavGraph>(start = true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
	val scrollState = rememberScrollState()
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
			.verticalScroll(scrollState),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		Greeting(
			name = if (BuildConfig.DEBUG) "Dev" else "User",
			fontSize = 20,
			isBold = true
		)
		AboutScreen()
		IconButton(
			onClick = { navigator.navigate(ChatDialogDestination)},
			modifier= Modifier
				.size(48.dp)
				.align(Alignment.End)
				.background(
					color = MaterialTheme.colorScheme.primary,
					shape = CircleShape
				),
		) {
			Icon(
				Icons.Default.Assistant,
				contentDescription = null,
				tint = MaterialTheme.colorScheme.onPrimary
			)
		}
	}
}

@Composable
fun AboutScreen() {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(20.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Text(
			text = "This app is an extension of the IDLGS platform that helps you stay connected to your school life in one place.",
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.height(32.dp))
		Text(
			text = "It securely loads your academic data using the IDLGS API, ensuring fast and reliable access.",
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.height(32.dp))
		Text(
			text = "IDLGS is an all-in-one digital learning and grading system where you can access study materials, track assignments, and view grades—all designed to make learning simpler and more organized.",
			style = MaterialTheme.typography.bodyLarge
		)
		Spacer(modifier = Modifier.height(32.dp))
		LinkToUrlButton(BuildConfig.WEBSITE_URL, "IDLGS platform")
	}
}

@SuppressLint("ModifierParameter")
@Composable
fun LinkToUrlButton(
	url: String,
	text: String = url,
	modifier: Modifier = Modifier.fillMaxWidth(.8f),
	context: Context = LocalContext.current
) {
	val openUrl = { context.openLinkInBrowser(url) }
	TextButton(
		onClick = openUrl,
		modifier = modifier,
	) {
		Text(text)
	}
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
	IDLGSTheme {
		HomeScreen(EmptyDestinationsNavigator)
	}
}