package cz.idlgs.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import cz.idlgs.mobile.data.UserSession
import cz.idlgs.mobile.domain.repository.AuthRepository
import cz.idlgs.mobile.utils.UiEventManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val authRepository: AuthRepository,
	private val userSession: UserSession,
	private val uiEventManager: UiEventManager,
) : ViewModel() {
	val name get() = userSession.name
	val surname get() = userSession.surname
	val email get() = userSession.email

	fun updateDetails(newName: String, newSurname: String) {
		userSession.name = newName
		userSession.surname = newSurname
		viewModelScope.launch {
			uiEventManager.showSnackbar("Details updated")
		}
	}

	fun logout(navigator: DestinationsNavigator) {
		viewModelScope.launch {
			val result = authRepository.logout()
			if (result.isSuccess) {
				navigator.navigate(LoginScreenDestination) {
					popUpTo(NavGraphs.root) { inclusive = true }
					launchSingleTop = true
				}
				userSession.name = ""
				userSession.surname = ""
				userSession.email = ""
				uiEventManager.showSnackbar("Logged out")
			} else {
				uiEventManager.showSnackbar("Logout failed")
			}
		}
	}
}
