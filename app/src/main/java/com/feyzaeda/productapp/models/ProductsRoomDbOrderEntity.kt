package com.feyzaeda.productapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductsRoomDbOrderEntity(
    @PrimaryKey val id: Int?,
    var title: String?,
    var description: String?,
    var price: Int?,
    var discountPercentage: Double?,
    var rating: Double?,
    var stock: Int?,
    var brand: String?,
    var category: String?,
    var thumbnail: String?,
    var images: List<String>,
    var productsPiece: Int = 1
)