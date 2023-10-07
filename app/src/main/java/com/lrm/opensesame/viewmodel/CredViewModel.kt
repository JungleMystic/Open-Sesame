package com.lrm.opensesame.viewmodel

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lrm.opensesame.database.CredDao
import com.lrm.opensesame.database.LoginCred
import kotlinx.coroutines.launch

class CredViewModel(private val credDao: CredDao): ViewModel() {

    val getAllCredentials = credDao.getAllCredentials().asLiveData()

    private fun insertCred(cred: LoginCred) {
        viewModelScope.launch { credDao.insert(cred) }
    }

    fun addCred(group: String, userName: String, password: String) {
        val cred = LoginCred(group = group, userName = userName, password = password)
        insertCred(cred)
    }

    fun isEntryValid(context: Context, group: String, userName: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(group) -> {
                Toast.makeText(context, "Please enter group name", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(userName) -> {
                Toast.makeText(context, "Please enter group name", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(context, "Please enter group name", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private val _groupNameList = MutableLiveData<List<String>>()
    val groupNameList: LiveData<List<String>> get() = _groupNameList

    fun setGroupNameList(credList: List<LoginCred>) {
        val nameList = mutableListOf<String>()

        credList.forEach { cred ->
            nameList.add(cred.group)
        }

        _groupNameList.postValue(nameList.distinct())
    }
}

class CredViewModelFactory(private val credDao: CredDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CredViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CredViewModel(credDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}