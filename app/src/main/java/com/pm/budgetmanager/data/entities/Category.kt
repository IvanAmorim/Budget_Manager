package com.pm.budgetmanager.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "category")
class Category (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val transactionType: String

    ):Parcelable