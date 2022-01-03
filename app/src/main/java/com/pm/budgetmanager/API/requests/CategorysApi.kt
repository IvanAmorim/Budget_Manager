package com.pm.budgetmanager.API.requests

import com.pm.budgetmanager.API.dto.CategoryDto
import com.pm.budgetmanager.API.models.Categorys
import retrofit2.Call
import retrofit2.http.*

interface CategorysApi {
    @GET("category/read")
    fun getCategorys(
        @Header("Authorization") token : String): Call<List<Categorys>>

    @FormUrlEncoded
    @POST("category/create")
    fun createCategorys(
        @Header("Authorization") token : String,
        @Field("name") name :String,
        @Field("transactionType") transactionType : String,
        @Field("users_id") users_id : String?
    ):Call<CategoryDto>

    @FormUrlEncoded
    @POST("category/update")
    fun updateCategorys(
        @Header("Authorization") token : String,
        @Field("id") id : Int,
        @Field("name") name :String,
        @Field("transactionType") transactionType : String,
    ):Call<CategoryDto>

    @FormUrlEncoded
    @POST("category/delete")
    fun deleteCategory(
        @Header("Authorization") token : String,
        @Field("id") id : Int
    ):Call<CategoryDto>



}