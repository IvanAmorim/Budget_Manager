package com.pm.budgetmanager.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Transactions")

class Transactions (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(defaultValue = "")
    val transactionCategory: String,
    @ColumnInfo(defaultValue = "")
    val account: String,
    val date: String,
    val comments: String,
    val value: Float

    ):Parcelable