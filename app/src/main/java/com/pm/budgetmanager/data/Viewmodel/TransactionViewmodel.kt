package com.pm.budgetmanager.data.Viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pm.budgetmanager.data.DB.db
import com.pm.budgetmanager.data.Repository.TransactionRepository
import com.pm.budgetmanager.data.entities.Transactions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewmodel(application: Application): AndroidViewModel(application){
    val readAllTransactions: LiveData<List<Transactions>>
    private val repository: TransactionRepository

    init {
        val transactionsDao = db.getDatabase(application).transactionsDao()
        repository = TransactionRepository(transactionsDao)
        readAllTransactions = repository.readAllTransactions
    }

    fun addTransaction(transaction: Transactions){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransactions(transaction)
        }
    }

    fun updateTransaction(transaction: Transactions){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransactions(transaction)
        }
    }

    fun deleteTransaction(transaction: Transactions){
        viewModelScope.launch(Dispatchers.IO){
            repository.addTransactions(transaction)
        }
    }
}