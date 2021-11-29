package com.pm.budgetmanager.data.Viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pm.budgetmanager.data.DB.db
import com.pm.budgetmanager.data.Repository.CategoryRepository
import com.pm.budgetmanager.data.entities.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewmodel(application: Application): AndroidViewModel(application) {
    val readAllCategorys: LiveData<List<Category>>

    private val repository: CategoryRepository

    init {
        val categoryDao = db.getDatabase(application).categoryDao()
        repository = CategoryRepository(categoryDao)
        readAllCategorys = repository.readAllCategorys
    }



    fun addCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            repository.addCategory(category)
        }
    }

    fun updateCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteCategory(category)
        }
    }

}