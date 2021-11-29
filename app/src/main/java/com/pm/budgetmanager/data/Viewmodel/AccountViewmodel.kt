package com.pm.budgetmanager.data.Viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pm.budgetmanager.data.DB.db
import com.pm.budgetmanager.data.Repository.AccountRepository
import com.pm.budgetmanager.data.entities.Accounts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewmodel(application: Application) : AndroidViewModel(application) {
    val readAllAccounts: LiveData<List<Accounts>>
    private val repository: AccountRepository

    init {
        val accountDao = db.getDatabase(application).accountDao()
        repository = AccountRepository(accountDao)
        readAllAccounts = repository.readAllAccounts
    }

    fun addAccount(account: Accounts){
        viewModelScope.launch(Dispatchers.IO){
            repository.addAccount(account)
        }
    }

    fun updateAccount(account: Accounts){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAccount(account)
        }
    }

    fun deleteAccount(account: Accounts){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAccount(account)
        }
    }

}
