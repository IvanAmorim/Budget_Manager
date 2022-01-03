package com.pm.budgetmanager.API.requests

import com.pm.budgetmanager.API.dto.AccountDto
import com.pm.budgetmanager.API.models.Account
import retrofit2.Call
import retrofit2.http.*

interface AccountApi {
    @GET("accounts/read")
    fun getAccounts(
        @Header("Authorization") token: String): Call<List<Account>>

    @FormUrlEncoded
    @POST("accounts/create")
    fun createAccount(
        @Header("Authorization") token: String,
        @Field("balance") balance:Float,
        @Field("name") name: String,
        @Field("users_id") users_id: String?,
    ): Call<AccountDto>

    @FormUrlEncoded
    @POST("accounts/update")
    fun updateAccount(
        @Header("Authorization") token: String,
        @Field("id") id:Int,
        @Field("name") name: String,
        @Field("balance") balance:Float,
    ): Call<AccountDto>

    @FormUrlEncoded
    @POST("accounts/delete")
    fun deleteAccount(
        @Header("Authorization") token: String,
        @Field("id") id: Int
    ): Call<AccountDto>
}