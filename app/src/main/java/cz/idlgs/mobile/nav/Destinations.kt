package cz.idlgs.mobile.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
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

val guestDestinations = listOf(
	NavDestination(R.string.about, Icons.Outlined.Info, HomeScreenDestination),
	NavDestination(R.string.login, Icons.Default.AccountBox, LoginScreenDestination),
)
val userDestinations = listOf(
	NavDestination(R.string.home, Icons.Default.Home, ListScreenDestination),
	NavDestination(R.string.profile, Icons.Default.AccountCircle, ProfileScreenDestination),
)
