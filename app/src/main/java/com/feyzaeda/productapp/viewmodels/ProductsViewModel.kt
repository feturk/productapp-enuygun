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
class ProductsViewModel @Inject constructor(private val mRepo: ProductsDaoRepo) : ViewModel() {
    private var _productsList = MutableLiveData<List<Products>>()
    val productsList: LiveData<List<Products>> get() = _productsList

    private var _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> get() = _categories

    val filteredProductLiveData = MutableLiveData<List<Products>>()

    private var minPrice: Int? = null
    private var maxPrice: Int? = null

    init {
        getProductsList()
        _productsList = mRepo.getProductsList()
        _categories = mRepo.getCategories()
    }


    fun getProductsList() = viewModelScope.launch {
        mRepo.allProducts()
    }
    fun getSelectCategory(category: String) = viewModelScope.launch {
        mRepo.selectCategories(category)
    }

    fun getSearchList(search: String) = viewModelScope.launch {
        mRepo.allSearchResult(search)
    }

    fun getCategories() = viewModelScope.launch {
        mRepo.allCategories()
    }

    fun productsListUpdate() {
        val emptyList: List<Products> = mutableListOf()
        _productsList.value = emptyList
        getProductsList()
        _productsList = mRepo.getProductsList()
    }

    fun productsCategoryListUpdate(category: String) {
        val emptyList: List<Products> = mutableListOf()
        _productsList.value = emptyList
        getSelectCategory(category)
        _productsList = mRepo.getSelectedCategories()
    }

    private fun applyPriceFilter(list: List<Products>): List<Products> {
        return list.filter { car ->
            val carPrice = car.price
            (minPrice == null || carPrice!! >= minPrice!!) && (maxPrice == null || carPrice!! <= maxPrice!!)
        }
    }
    fun updatePriceFilter(min: Int?, max: Int?) {
        minPrice = min
        maxPrice = max
        filteredProductLiveData.value = applyPriceFilter(_productsList.value!!)
    }
}