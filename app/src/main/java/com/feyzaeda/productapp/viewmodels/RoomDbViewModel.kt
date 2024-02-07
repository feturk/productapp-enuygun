package com.feyzaeda.productapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity
import com.feyzaeda.productapp.models.ProductsRoomDbOrderEntity
import com.feyzaeda.productapp.repo.ProductsDbRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RoomDbViewModel @Inject constructor(private val roomDBRepo: ProductsDbRepo) : ViewModel() {
    private var _favProductsList = MutableLiveData<MutableList<ProductsRoomDbFavEntity>>()
    val favProductsList: MutableLiveData<MutableList<ProductsRoomDbFavEntity>> get() = _favProductsList

    private var _orderProductsList = MutableLiveData<MutableList<ProductsRoomDbOrderEntity>>()
    val orderProductsList: MutableLiveData<MutableList<ProductsRoomDbOrderEntity>> get() = _orderProductsList

    private val _totalPrice = MutableLiveData<Int>()
    val totalPrice: LiveData<Int> get() = _totalPrice

    init {
        loadFavProducts()
        loadOrderProducts()
        _favProductsList = roomDBRepo.getFav()
        _orderProductsList = roomDBRepo.getOrder()
    }

    fun loadFavProducts() {
        roomDBRepo.allFavData()
    }

    fun insertFavProducts(roomDBEntity: ProductsRoomDbFavEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomDBRepo.insertFavProducts(roomDBEntity)
            }
        }
    }

    fun deleteFavProducts(roomDBEntity: ProductsRoomDbFavEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomDBRepo.deleteFavProducts(roomDBEntity)
            }
        }
    }

    fun loadOrderProducts() {
        roomDBRepo.allOrderData()
    }

    fun insertOrderProducts(roomDBEntity: ProductsRoomDbOrderEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomDBRepo.insertOrderProducts(roomDBEntity)
            }
        }
    }

    fun deleteOrderProducts(roomDBEntity: ProductsRoomDbOrderEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomDBRepo.deleteOrderProducts(roomDBEntity)
            }
        }
    }

    fun updateOrderProducts(roomDBEntity: ProductsRoomDbOrderEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomDBRepo.updateOrderProducts(roomDBEntity)
            }
        }
    }

    fun allDeleteOrderProducts() {
        val cartItemsList = _orderProductsList.value ?: emptyList()
        for (cardItem in cartItemsList) {
            roomDBRepo.deleteOrderProducts(cardItem)
        }
    }

    fun updateTotalValues() {
        val cartItemsList = _orderProductsList.value ?: emptyList()
        var total = 0

        for (cartItem in cartItemsList) {
            total += cartItem.productsPiece * cartItem.price!!
        }
        _totalPrice.value = total
    }

}