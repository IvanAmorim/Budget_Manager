package com.pm.budgetmanager.data.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pm.budgetmanager.data.entities.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Query("SELECT * FROM category ORDER BY id DESC")
    fun readAllCategorys(): LiveData<List<Category>>

    @Delete
    fun deleteCategory(category: Category)


}