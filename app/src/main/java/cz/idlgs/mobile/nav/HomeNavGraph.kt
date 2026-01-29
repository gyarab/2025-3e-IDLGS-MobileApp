package cz.idlgs.mobile.nav

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootGraph

@NavGraph<RootGraph>(start = true, defaultTransitions = HorizontalSlideTransitions::class)
annotation class HomeNavGraph