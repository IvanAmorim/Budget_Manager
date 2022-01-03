package com.pm.budgetmanager.API.dto

import com.pm.budgetmanager.API.models.Categorys


data class CategoryDto (
    val status : String,
    val message : String,
    val category: Categorys
        )