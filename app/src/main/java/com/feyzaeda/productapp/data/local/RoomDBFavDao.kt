package com.feyzaeda.productapp.data.local

import androidx.room.*
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity

@Dao
interface RoomDBFavDao {
    @Insert
    fun insertFav(entity: ProductsRoomDbFavEntity)

    @Query("SELECT * FROM favori")
    fun getAllFavEntities(): MutableList<ProductsRoomDbFavEntity>

    @Delete
    fun deleteFav(entityDelete: ProductsRoomDbFavEntity)
}