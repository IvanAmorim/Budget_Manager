package com.pm.budgetmanager.API.dto

import com.pm.budgetmanager.API.models.User

data class UserDto (
    val status : String,
    val message : String,
    val user : List<User>,
    val token : String
)
