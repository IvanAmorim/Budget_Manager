package com.pm.budgetmanager.API.dto

import com.pm.budgetmanager.API.models.Transactions

data class TransactionsDto (
    val status : String,
    val message : String,
    val transaction: Transactions
        )