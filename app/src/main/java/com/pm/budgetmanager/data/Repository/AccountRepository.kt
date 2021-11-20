package com.pm.budgetmanager.data.Repository

import android.accounts.Account
import androidx.lifecycle.LiveData
import com.pm.budgetmanager.data.Dao.AccountDao
import com.pm.budgetmanager.data.entities.Accounts

class AccountRepository(private val accountDao: AccountDao) {
    val readAllAccounts: LiveData<List<Accounts>> = accountDao.readAllAccounts()

    suspend fun addAccount(account: Accounts){
        accountDao.addAccount(account)
    }

    suspend fun updateAccount(account: Accounts){
        accountDao.updateAccount(account)
    }

    suspend fun deleteAccount(account: Accounts){
        accountDao.deleteAccount(account)
    }
}