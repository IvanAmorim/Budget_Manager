package com.pm.budgetmanager.data.Repository

import androidx.lifecycle.LiveData
import com.pm.budgetmanager.data.Dao.CategoryDao
import com.pm.budgetmanager.data.entities.Category

class CategoryRepository(private val categoryDao: CategoryDao) {
    val readAllCategorys: LiveData<List<Category>> = categoryDao.readAllCategorys()

    suspend fun addCategory(category: Category){
        categoryDao.addCategory(category)
    }

    suspend fun updateCategory(category: Category){
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(category: Category){
        categoryDao.deleteCategory(category)
    }

}