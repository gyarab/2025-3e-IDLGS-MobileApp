package cz.idlgs.mobile.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

object UiUtils {

	@OptIn(ExperimentalLayoutApi::class)
	@Composable
	fun isImeOpen() = WindowInsets.isImeVisible

	@Composable
	fun orientation() = LocalConfiguration.current.orientation

	@Composable
	fun adaptiveNavSuiteType() = NavigationSuiteScaffoldDefaults.navigationSuiteType(
		currentWindowAdaptiveInfo()
	)

	@Composable
	fun SplitIfLandscape(
		isLandscape: Boolean,
		modifier: Modifier = Modifier,
		header: @Composable () -> Unit,
		form: @Composable () -> Unit
	) {
		val scrollState = rememberScrollState()
		if (isLandscape) {
			Row(
				modifier = modifier
					.fillMaxSize()
					.padding(horizontal = 16.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Column(
					modifier = Modifier.weight(1f),
					horizontalAlignment = Alignment.CenterHorizontally
				) { header() }

				Spacer(Modifier.width(24.dp))

				Column(
					modifier = Modifier
						.weight(1f)
						.verticalScroll(scrollState),
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					form()
				}
			}
		} else {
			Column(
				modifier = modifier
					.fillMaxSize()
					.padding(horizontal = 16.dp)
					.verticalScroll(scrollState),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Spacer(Modifier.height(16.dp))
				header()
				Spacer(Modifier.height(32.dp))
				form()
			}
		}
	}
}