package cz.idlgs.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.idlgs.mobile.BuildConfig
import cz.idlgs.mobile.Greeting
import cz.idlgs.mobile.ui.theme.IDLGSTheme
import cz.idlgs.mobile.utils.Utils.openLinkInBrowser

@Composable
fun HomeScreen(onChatClick: () -> Unit) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Greeting(
			name = if (BuildConfig.DEBUG) "Dev" else "User",
			modifier = Modifier
				.padding(top = 40.dp)
				.align(Alignment.TopCenter),
			fontSize = 20,
			isBold = true
		)
		AboutScreen()
		IconButton(
			onClick = onChatClick,
			modifier = Modifier
				.size(48.dp)
				.align(Alignment.BottomEnd)
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
		verticalArrangement = Arrangement.Center
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
		LinkToIDLGS()
	}
}

@Composable
fun LinkToIDLGS() {
	val context = LocalContext.current
	val openUrl: () -> Unit =
		{ openLinkInBrowser(context = context, url = "https://ucebnice.martinbykov.eu") }
	TextButton(
		onClick = openUrl,
		modifier = Modifier.fillMaxWidth(.8f),
	) {
		Text("IDLGS platform")
	}
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
	IDLGSTheme {
		HomeScreen {}
	}
}