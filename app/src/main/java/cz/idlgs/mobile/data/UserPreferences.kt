package cz.idlgs.mobile.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class UserPreferences @Inject constructor(
	@ApplicationContext private val context: Context
) {
	private val EMAIL_KEY = stringPreferencesKey("user_email")

	val userEmail: Flow<String?> = context.dataStore.data
		.map { preferences ->
			preferences[EMAIL_KEY]
		}

	suspend fun saveEmail(email: String) = context.dataStore
		.edit { preferences ->
			preferences[EMAIL_KEY] = email
		}
}
