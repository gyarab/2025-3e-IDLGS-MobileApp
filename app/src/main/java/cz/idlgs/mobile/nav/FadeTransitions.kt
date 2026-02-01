package cz.idlgs.mobile.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

object FadeTransitions : NavHostAnimatedDestinationStyle() {
	override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
		{
			fadeIn(animationSpec = tween(300))
		}
	override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
		{
			fadeOut(animationSpec = tween(300))
		}
	override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
		{
			fadeIn(animationSpec = tween(300))
		}
	override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
		{
			fadeOut(animationSpec = tween(300))
		}
}