package com.pm.budgetmanager.data.Repository

import androidx.lifecycle.LiveData
import com.pm.budgetmanager.data.Dao.TransactionsDao
import com.pm.budgetmanager.data.entities.Transactions

class TransactionRepository(private val transactionsDao: TransactionsDao) {
    val readAllTransactions: LiveData<List<Transactions>> = transactionsDao.readAllTransactions()

    suspend fun addTransactions(transaction: Transactions){
        transactionsDao.addTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transactions){
        transactionsDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transactions){
        transactionsDao.deleteTransaction(transaction)
    }


}