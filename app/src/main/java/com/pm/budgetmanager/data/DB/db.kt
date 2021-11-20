package com.pm.budgetmanager.data.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pm.budgetmanager.data.Dao.AccountDao
import com.pm.budgetmanager.data.Dao.CategoryDao
import com.pm.budgetmanager.data.Dao.TransactionsDao
import com.pm.budgetmanager.data.entities.Accounts
import com.pm.budgetmanager.data.entities.Category
import com.pm.budgetmanager.data.entities.Transactions

@Database(entities = [Accounts::class, Transactions::class, Category::class], version = 1, exportSchema = false)
abstract class db: RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile
        private var INSTANCE: db? = null

        fun getDatabase(context: Context):db{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    db::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}