package com.feyzaeda.productapp.data.local

import androidx.room.*
import com.feyzaeda.productapp.models.ProductsRoomDbOrderEntity

@Dao
interface RoomDbOrderDao {
    @Insert
    fun insertOrder(entity: ProductsRoomDbOrderEntity)

    @Query("SELECT * FROM products")
    fun getAllOrderEntities(): MutableList<ProductsRoomDbOrderEntity>

    @Delete
    fun deleteOrder(entityDelete: ProductsRoomDbOrderEntity)

    @Update
    fun updateProduct(entityUpdate: ProductsRoomDbOrderEntity)
}