package com.feyzaeda.productapp.models

import com.google.gson.annotations.SerializedName

data class BaseModel(
    @SerializedName("products") var products: List<Products>? = null,
    @SerializedName("total") var total: Int? = null,
    @SerializedName("skip") var skip: Int? = null,
    @SerializedName("limit") var limit: Int? = null,
)
