package com.example.myfinalproject_capstone.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ID_KEY = stringPreferencesKey("id")
    private val NAME_KEY = stringPreferencesKey("name")
    private val EMAIL_KEY = stringPreferencesKey("email")
    private val PASSWORD_KEY = stringPreferencesKey("password")
    private val CODECOMPANY_KEY = stringPreferencesKey("codeCompany")
    private val POSITION_KEY = stringPreferencesKey("position")

    fun getID(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[ID_KEY] ?: "Null"
        }
    }

    fun getCompanyID(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CODECOMPANY_KEY] ?: "Null"
        }
    }

    fun getNameStaff(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: "Null"
        }
    }

    fun getPosition(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[POSITION_KEY] ?: "Null"
        }
    }

    suspend fun saveUserSetting(id: String, name: String, email: String, password: String, codeCompany: String, position: String) {
        dataStore.edit { preferences ->
            preferences[ID_KEY] = id
            preferences[NAME_KEY] = name
            preferences[EMAIL_KEY] = email
            preferences[PASSWORD_KEY] = password
            preferences[CODECOMPANY_KEY] = codeCompany
            preferences[POSITION_KEY] = position
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
