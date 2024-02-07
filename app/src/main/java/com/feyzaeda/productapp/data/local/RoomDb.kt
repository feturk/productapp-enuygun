package com.feyzaeda.productapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity
import com.feyzaeda.productapp.models.ProductsRoomDbOrderEntity
import com.feyzaeda.productapp.util.Converters

@Database(entities = [ProductsRoomDbFavEntity::class, ProductsRoomDbOrderEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class RoomDb : RoomDatabase() {

    abstract fun productsFavDao(): RoomDBFavDao

    abstract fun productsOrderDao(): RoomDbOrderDao
}