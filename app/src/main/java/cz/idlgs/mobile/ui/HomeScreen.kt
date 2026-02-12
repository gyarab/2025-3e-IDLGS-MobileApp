package cz.idlgs.mobile.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.ChatDialogDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import cz.idlgs.mobile.BuildConfig
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
		AboutScreen()
	}
	Box(Modifier.fillMaxSize()) {
		IconButton(
			onClick = { navigator.navigate(ChatDialogDestination) },
			modifier = Modifier
				.padding(12.dp)
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
		verticalArrangement = Arrangement.Center,
	) {
		Text(
			text = buildString {
				append("Welcome to IDLGS Mobile")
				if (BuildConfig.DEBUG) append(", Dev")
			},
			style = MaterialTheme.typography.headlineMedium,
			fontWeight = FontWeight.Bold,
			color = MaterialTheme.colorScheme.primary,
		)

		Spacer(modifier = Modifier.height(24.dp))

		InfoCard(
			icon = Icons.Default.School,
			title = "Stay Connected",
			description = "The ultimate extension of the IDLGS platform, keeping your school life organized in one place."
		)

		InfoCard(
			icon = Icons.Default.Lock,
			title = "Secure & Fast",
			description = "Real-time data syncing via the IDLGS API ensures your academic records are always up to date."
		)

		InfoCard(
			icon = Icons.Default.AutoGraph,
			title = "Track Your Progress",
			description = "Access study materials, track assignments, and view grades in a simplified digital environment."
		)

		Spacer(modifier = Modifier.height(16.dp))
		LinkToUrlButton(BuildConfig.WEBSITE_URL, "IDLGS platform")
	}
}

@Composable
fun InfoCard(icon: ImageVector, title: String, description: String) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
		)
	) {
		Row(
			modifier = Modifier.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier.size(32.dp)
			)
			Spacer(modifier = Modifier.width(16.dp))
			Column {
				Text(
					text = title,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.Bold
				)
				Text(text = description, style = MaterialTheme.typography.bodyMedium)
			}
		}
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