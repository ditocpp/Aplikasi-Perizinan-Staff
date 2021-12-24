package com.example.myfinalproject_capstone.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getID(): LiveData<String> {
        return pref.getID().asLiveData()
    }

    fun getCompanyID(): LiveData<String> {
        return pref.getCompanyID().asLiveData()
    }

    fun getNameStaff(): LiveData<String> {
        return pref.getNameStaff().asLiveData()
    }

    fun getPosition(): LiveData<String> {
        return pref.getPosition().asLiveData()
    }

    fun saveUserSetting(id: String, name: String, email: String, password: String, codeCompany: String, position: String) {
        viewModelScope.launch {
            pref.saveUserSetting(id, name, email, password, codeCompany, position)
        }
    }
}