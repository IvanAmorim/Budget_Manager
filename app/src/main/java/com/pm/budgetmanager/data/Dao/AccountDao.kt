package com.pm.budgetmanager.data.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pm.budgetmanager.data.entities.Accounts

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAccount(accounts: Accounts)

    @Update
    fun updateAccount(accounts: Accounts)

    @Query("SELECT * FROM accounts ORDER BY id ASC ")
    fun readAllAccounts(): LiveData<List<Accounts>>

    @Delete
    fun deleteAccount(accounts: Accounts)
}