package com.example.feature.auth.data.session

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore(name = "session_prefs")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    }

    // Fluxul ne spune daca utilizatorul este logat.
    val isLoggedInFlow: Flow<Boolean> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_IS_LOGGED_IN] ?: false
    }

    val userNameFlow: Flow<String?> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_USER_NAME]
    }

    val userEmailFlow: Flow<String?> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_USER_EMAIL]
    }

    suspend fun setLoggedIn(value: Boolean, userName: String? = null, userEmail: String? = null) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = value
            if (userName != null) {
                prefs[KEY_USER_NAME] = userName
            }
            if (userEmail != null) {
                prefs[KEY_USER_EMAIL] = userEmail
            }
            
            if (!value) {
                prefs.remove(KEY_USER_NAME)
                prefs.remove(KEY_USER_EMAIL)
            }
        }
    }

    suspend fun updateName(newName: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = newName
        }
    }
}
