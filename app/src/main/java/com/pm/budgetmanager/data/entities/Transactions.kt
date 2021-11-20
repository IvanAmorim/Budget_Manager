package com.pm.budgetmanager.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Transactions")

class Transactions (
    @PrimaryKey(autoGenerate = true)

    val id: Int,
    val transactionCategory: Int,
    val account: Int,
    val date: String,
    val comments: String,
    val value: Float

    ):Parcelable