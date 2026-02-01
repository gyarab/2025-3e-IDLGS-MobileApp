package cz.idlgs.mobile.nav

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle

object HorizontalSlideTransitions : NavHostAnimatedDestinationStyle() {
	override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
		{
			slideInHorizontally(
				initialOffsetX = { it },
				animationSpec = tween(300)
			) + fadeIn(animationSpec = tween(600))
		}
	override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
		{
			slideOutHorizontally(
				targetOffsetX = { -it },
				animationSpec = tween(300)
			) + fadeOut(animationSpec = tween(600))
		}
	override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
		{
			slideInHorizontally(
				initialOffsetX = { -it },
				animationSpec = tween(300)
			) + fadeIn(animationSpec = tween(600))
		}
	override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
		{
			slideOutHorizontally(
				targetOffsetX = { it },
				animationSpec = tween(300)
			) + fadeOut(animationSpec = tween(600))
		}
}