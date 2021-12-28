package com.pm.budgetmanager.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
/*
@Parcelize
@Entity(foreignKeys = arrayOf(ForeignKey(
    entity = Category::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("transactionCategory"),
    onDelete = ForeignKey.CASCADE)),
    tableName = "Transactions")
class Transactions (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    //@ColumnInfo(defaultValue = "")
    val transactionCategory: String,
    @ColumnInfo(defaultValue = "")
    val account: String,
    val date: String,
    val comments: String,
    val value: Float

    ):Parcelable*/
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
