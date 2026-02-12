package cz.idlgs.mobile.nav

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootGraph

@NavGraph<RootGraph>(start = true, defaultTransitions = FadeTransitions::class)
annotation class AuthNavGraph

@NavGraph<RootGraph>(defaultTransitions = FadeTransitions::class)
annotation class ChatNavGraph

@NavGraph<RootGraph>(defaultTransitions = FadeTransitions::class)
annotation class GuestNavGraph

@NavGraph<RootGraph>(defaultTransitions = HorizontalSlideTransitions::class)
annotation class UserNavGraph