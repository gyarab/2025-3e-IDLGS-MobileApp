package cz.idlgs.mobile.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
	val isLoggedIn: StateFlow<Boolean>
	suspend fun login(email: String, password: String): Result<Unit>
	suspend fun logout(): Result<Unit>
}