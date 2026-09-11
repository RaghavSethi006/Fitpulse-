package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ApiKeyManager(context: Context) {

    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val _customKey = MutableStateFlow(getSavedCustomKey())
    val customKey: StateFlow<String> = _customKey.asStateFlow()

    private val _activeKey = MutableStateFlow(computeActiveKey())
    val activeKey: StateFlow<String> = _activeKey.asStateFlow()

    private val _hasActiveKey = MutableStateFlow(computeActiveKey().isNotBlank())
    val hasActiveKey: StateFlow<Boolean> = _hasActiveKey.asStateFlow()

    private fun getSavedCustomKey(): String {
        return prefs.getString(KEY_GEMINI_CUSTOM, "")?.trim() ?: ""
    }

    private fun computeActiveKey(): String {
        val custom = getSavedCustomKey()
        if (custom.isNotBlank()) {
            return custom
        }
        val buildKey = try {
            BuildConfig.GEMINI_API_KEY.trim()
        } catch (_: Exception) {
            ""
        }
        if (buildKey.isNotBlank() && buildKey != "MY_GEMINI_API_KEY") {
            return buildKey
        }
        return ""
    }

    fun getActiveKey(): String {
        return computeActiveKey()
    }

    fun isCustomKeyActive(): Boolean {
        return getSavedCustomKey().isNotBlank()
    }

    fun isBuildKeyActive(): Boolean {
        return !isCustomKeyActive() && computeActiveKey().isNotBlank()
    }

    fun saveCustomKey(key: String) {
        val trimmed = key.trim()
        prefs.edit().putString(KEY_GEMINI_CUSTOM, trimmed).apply()
        _customKey.value = trimmed
        val active = computeActiveKey()
        _activeKey.value = active
        _hasActiveKey.value = active.isNotBlank()
    }

    fun clearCustomKey() {
        prefs.edit().remove(KEY_GEMINI_CUSTOM).apply()
        _customKey.value = ""
        val active = computeActiveKey()
        _activeKey.value = active
        _hasActiveKey.value = active.isNotBlank()
    }

    companion object {
        private const val PREFS_NAME = "fitpulse_api_keys"
        private const val KEY_GEMINI_CUSTOM = "custom_gemini_api_key"

        @Volatile
        private var instance: ApiKeyManager? = null

        fun getInstance(context: Context): ApiKeyManager {
            return instance ?: synchronized(this) {
                instance ?: ApiKeyManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
