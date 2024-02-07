package com.feyzaeda.productapp.data.remote

import com.feyzaeda.productapp.models.BaseModel
import com.feyzaeda.productapp.models.Products
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("products")
    suspend fun getProductsList(): Response<BaseModel>

    @GET("products/search")
    suspend fun getProductsList(@Query("q") search: String): Response<BaseModel>

    @GET("products/{id}")
    suspend fun getSingleProduct(@Path("id") id: Int): Response<Products>

    @GET("products/categories")
    suspend fun getCategories(): Response<List<String>>
    @GET("products/category/{category}")
    suspend fun selectCategories(@Path("category") category: String): Response<BaseModel>
}