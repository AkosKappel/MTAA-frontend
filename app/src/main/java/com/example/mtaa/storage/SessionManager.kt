package com.example.mtaa.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.mtaa.R

class SessionManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val PROFILE_PICTURE = "profile_picture"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun deleteAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        return fetchAuthToken() != null
    }

    fun saveUserId(id: String) {
        val editor = prefs.edit()
        editor.putString(USER_ID, id)
        editor.apply()
    }

    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun deleteUserId() {
        val editor = prefs.edit()
        editor.remove(USER_ID)
        editor.apply()
    }

    fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun deleteUserEmail() {
        val editor = prefs.edit()
        editor.remove(USER_EMAIL)
        editor.apply()
    }

    fun saveProfilePicturePath(picture: String) {
        val editor = prefs.edit()
        editor.putString(PROFILE_PICTURE, picture)
        editor.apply()
    }

    fun fetchProfilePicturePath(): String? {
        return prefs.getString(PROFILE_PICTURE, null)
    }

    fun deleteProfilePicturePath() {
        val editor = prefs.edit()
        editor.remove(PROFILE_PICTURE)
        editor.apply()
    }

    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
