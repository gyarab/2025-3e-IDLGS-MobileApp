package cz.idlgs.mobile.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.generated.destinations.*
import com.ramcosta.composedestinations.spec.Direction
import cz.idlgs.mobile.R

data class NavDestination(
	@StringRes val labelRes: Int,
	val icon: ImageVector,
	val direction: Direction,
)

object Destinations {
	val guest = listOf(
		NavDestination(R.string.login, Icons.Default.AccountBox, LoginScreenDestination),
		NavDestination(R.string.about, Icons.Outlined.Info, HomeScreenDestination),
	)
	val user = listOf(
		NavDestination(R.string.home, Icons.Default.Home, ListScreenDestination),
		NavDestination(R.string.profile, Icons.Default.AccountCircle, ProfileScreenDestination),
		NavDestination(R.string.settings, Icons.Default.Settings, SettingsScreenDestination),
	)
}
