package com.pm.budgetmanager.API.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account (
    val id : Int,
    var name: String,
    var balance: Float,
    val users_id : Int,
    val created_at :String,
    val user_name: String
        ):Parcelable