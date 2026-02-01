package cz.idlgs.mobile.viewmodel

import androidx.lifecycle.ViewModel
import cz.idlgs.mobile.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
	authRepository: AuthRepository
) : ViewModel() {
	val isLoggedIn = authRepository.isLoggedIn
}