package cz.idlgs.mobile.utils

import kotlinx.coroutines.delay

object AuthUtils {
	val emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9-]+\\.)+[A-Za-z0-9-]+".toRegex()
	fun isEmailFormatValid(email: String): Boolean = email.matches(emailRegex)

	suspend fun doesEmailExist(email: String): Boolean {
		delay(1500) // Simulate network request time
		// Call backend here
		return email == "user@example.com"
	}
}
