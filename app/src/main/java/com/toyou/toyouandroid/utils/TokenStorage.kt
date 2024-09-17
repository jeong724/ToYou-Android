package com.toyou.toyouandroid.utils

import android.content.Context

class TokenStorage(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit().apply {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)
    fun getRefreshToken(): String? = sharedPreferences.getString("refresh_token", null)
}