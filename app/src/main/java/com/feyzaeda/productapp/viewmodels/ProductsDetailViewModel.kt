package com.feyzaeda.productapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feyzaeda.productapp.models.Products
import com.feyzaeda.productapp.repo.ProductsDaoRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsDetailViewModel @Inject constructor(private val mRepo: ProductsDaoRepo) : ViewModel() {
    private var _singleItem = MutableLiveData<Products>()
    val singleItem: LiveData<Products> get() = _singleItem

    init {
        _singleItem = mRepo.getSingleItem()
    }

    fun getSingleItem(id: Int) = viewModelScope.launch {
        mRepo.singleProduct(id)
    }
}
