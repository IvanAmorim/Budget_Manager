package com.pm.budgetmanager.data.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pm.budgetmanager.data.entities.Transactions


@Dao
interface TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addTransaction(transaction: Transactions)

    @Update
    fun updateTransaction(transaction: Transactions)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun readAllTransactions(): LiveData<List<Transactions>>

    @Delete
    fun deleteTransaction (transaction: Transactions)

}