package com.pm.budgetmanager.API.dto

import android.accounts.Account

data class AccountDto (
    val status : String,
    val message : String,
    val account : Account
    )