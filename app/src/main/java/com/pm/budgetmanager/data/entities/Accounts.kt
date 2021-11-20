package com.pm.budgetmanager.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "Accounts")

class Accounts (
    @PrimaryKey(autoGenerate = true)
    val Id: Int,
    val name: String,
    val balance: Float
): Parcelable