package com.pm.budgetmanager.API.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Categorys (
    val id : Int,
    val name : String,
    val transactionType : String,
    val users_id : Int,
    val created_at : String
        ): Parcelable
