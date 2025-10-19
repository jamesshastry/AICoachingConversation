package com.aicoaching.conversation.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class ApiKeyManager(private val context: Context) {
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "api_keys",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    fun saveOpenAIKey(apiKey: String) {
        sharedPreferences.edit()
            .putString("openai_api_key", apiKey)
            .apply()
    }
    
    fun saveElevenLabsKey(apiKey: String) {
        sharedPreferences.edit()
            .putString("elevenlabs_api_key", apiKey)
            .apply()
    }
    
    fun getOpenAIKey(): String? {
        return sharedPreferences.getString("openai_api_key", null)
    }
    
    fun getElevenLabsKey(): String? {
        return sharedPreferences.getString("elevenlabs_api_key", null)
    }
    
    fun hasApiKeys(): Boolean {
        return getOpenAIKey() != null && getElevenLabsKey() != null
    }
    
    fun clearApiKeys() {
        sharedPreferences.edit()
            .remove("openai_api_key")
            .remove("elevenlabs_api_key")
            .apply()
    }
}
