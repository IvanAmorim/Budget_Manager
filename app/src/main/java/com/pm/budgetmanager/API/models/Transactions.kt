package com.pm.budgetmanager.API.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transactions (
    val id : Int,
    val category_id : Int,
    val accounts_id : Int,
    val date : String,
    val comments : String,
    val value : Float,
    val users_id : Int,
    val user_name : String,
    val category_name : String,
    val account_Name : String,
        ):Parcelable