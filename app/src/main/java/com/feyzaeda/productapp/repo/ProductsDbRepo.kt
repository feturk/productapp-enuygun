package com.feyzaeda.productapp.repo

import androidx.lifecycle.MutableLiveData
import com.feyzaeda.productapp.data.local.RoomDBFavDao
import com.feyzaeda.productapp.data.local.RoomDbOrderDao
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity
import com.feyzaeda.productapp.models.ProductsRoomDbOrderEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductsDbRepo @Inject constructor(private val appFavDao: RoomDBFavDao, private val appOrderDao: RoomDbOrderDao) {
    private var favProductsList: MutableLiveData<MutableList<ProductsRoomDbFavEntity>> = MutableLiveData()
    private var orderProductsList: MutableLiveData<MutableList<ProductsRoomDbOrderEntity>> = MutableLiveData()


    fun getFav(): MutableLiveData<MutableList<ProductsRoomDbFavEntity>> {
        return favProductsList
    }
    fun allFavData() {
        CoroutineScope(Dispatchers.Main).launch {
            favProductsList.value = appFavDao.getAllFavEntities()
        }
    }

    fun insertFavProducts(roomDBEntity: ProductsRoomDbFavEntity){
        return appFavDao.insertFav(roomDBEntity)
    }

    fun deleteFavProducts(roomDBEntity: ProductsRoomDbFavEntity){
        return appFavDao.deleteFav(entityDelete = roomDBEntity)
    }

    fun getOrder(): MutableLiveData<MutableList<ProductsRoomDbOrderEntity>> {
        return orderProductsList
    }

    fun allOrderData() {
        CoroutineScope(Dispatchers.Main).launch {
            orderProductsList.value = appOrderDao.getAllOrderEntities()
        }
    }

    fun insertOrderProducts(roomDBEntity: ProductsRoomDbOrderEntity){
        return appOrderDao.insertOrder(entity = roomDBEntity)
    }

    fun deleteOrderProducts(roomDBEntity: ProductsRoomDbOrderEntity){
        return appOrderDao.deleteOrder(entityDelete = roomDBEntity)
    }

    fun updateOrderProducts(roomDBEntity: ProductsRoomDbOrderEntity){
        return appOrderDao.updateProduct(entityUpdate = roomDBEntity)
    }
}