package com.pm.budgetmanager.API.requests

import com.pm.budgetmanager.API.dto.TransactionsDto
import com.pm.budgetmanager.API.models.Transactions
import com.pm.budgetmanager.data.Dao.TransactionsDao
import retrofit2.Call
import retrofit2.http.*

interface TransactionsApi {
    @GET("transactions/read")
    fun getTransactions(
        @Header("Authorization") token: String
    ): Call<List<Transactions>>

    @FormUrlEncoded
    @POST("transactions/create")
    fun createTransaction(
        @Header("Authorization") token : String,
        @Field("category_id") category_id : Int,
        @Field("accounts_id") accounts_id : Int,
        @Field("date") date : String,
        @Field("comments") comments : String,
        @Field("value") value : Float,
        @Field("users_id")users_id : String?
    ): Call<TransactionsDto>

    @FormUrlEncoded
    @POST("transactions/update")
    fun updateTransaction(
        @Header("Authorization") token : String,
        @Field("id") id :Int,
        @Field("category_id") category_id : Int,
        @Field("accounts_id") accounts_id : Int,
        @Field("date") date : String,
        @Field("comments") comments : String,
        @Field("value") value : Float,
    ): Call<TransactionsDto>

    @FormUrlEncoded
    @POST("transactions/delete")
    fun deleteTransaction(
        @Header("Authorization") token :String,
        @Field("id") id :Int
    ):Call<TransactionsDto>
}