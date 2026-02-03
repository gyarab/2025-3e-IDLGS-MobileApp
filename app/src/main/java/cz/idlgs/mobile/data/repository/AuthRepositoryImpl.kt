package cz.idlgs.mobile.data.repository

import cz.idlgs.mobile.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepositoryImpl : AuthRepository {

	private val _isLoggedIn = MutableStateFlow(false)
	override val isLoggedIn = _isLoggedIn.asStateFlow()

	override suspend fun login(email: String, password: String): Result<Unit> {
		delay(2000)
		_isLoggedIn.value = true
		println("AuthRepositoryImpl isLoggedIn: ${_isLoggedIn.value}")
		return try {
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override suspend fun logout(): Result<Unit> {
		delay(1000)
		_isLoggedIn.value = false
		println("AuthRepositoryImpl isLoggedIn: ${_isLoggedIn.value}")
		return try {
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}