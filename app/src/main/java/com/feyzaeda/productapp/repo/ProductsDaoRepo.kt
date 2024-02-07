package com.feyzaeda.productapp.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.feyzaeda.productapp.data.remote.APIService
import com.feyzaeda.productapp.models.Products
import javax.inject.Inject

class ProductsDaoRepo @Inject constructor(private val mDao: APIService) {
    private var productsList: MutableLiveData<List<Products>> = MutableLiveData()
    private var singleList: MutableLiveData<Products> = MutableLiveData()
    private var categories: MutableLiveData<List<String>> = MutableLiveData()

    fun getProductsList(): MutableLiveData<List<Products>> {
        return productsList
    }

    fun getSingleItem(): MutableLiveData<Products>{
        return singleList
    }

    fun getCategories(): MutableLiveData<List<String>>{
        return categories
    }
    fun getSelectedCategories(): MutableLiveData<List<Products>>{
        return productsList
    }


    suspend fun allProducts(){
        val response = mDao.getProductsList()
        if(response.isSuccessful){
            response.body()?.let {
                Log.e("allProducts api","girdi")
                val list = response.body()
                if (list != null) {
                    productsList.value = list.products!!
                }
            }

        }else{
            Log.e("allProducts api","hata")
        }
    }

    suspend fun allSearchResult(search: String){
        val response = mDao.getProductsList(search)
        if(response.isSuccessful){
            response.body()?.let {
                Log.e("allSearchResult api","girdi")
                val list = response.body()
                if (list != null) {
                    productsList.value = list.products!!
                }
            }

        }else{
            Log.e("allSearchResult api","hata")
        }
    }
    suspend fun singleProduct(id: Int){
        val response = mDao.getSingleProduct(id)
        if(response.isSuccessful){
            response.body()?.let {
                Log.e("singleProduct api","girdi")
                val singleProduct = response.body()
                if (singleProduct != null) {
                    singleList.value = singleProduct!!
                }
            }
        }else{
            Log.e("singleProduct api","hata")
        }
    }

    suspend fun allCategories(){
        val response = mDao.getCategories()
        if(response.isSuccessful){
            response.body()?.let {
                Log.e("getCategories api","girdi")
                val getCategories = response.body()
                if (getCategories != null) {
                    categories.value = getCategories!!
                }
            }
        }else{
            Log.e("getCategories api","hata")
        }
    }

    suspend fun selectCategories(category: String){
        val response = mDao.selectCategories(category)
        if(response.isSuccessful){
            response.body()?.let {
                Log.e("selectCategories api","girdi")
                val list = response.body()
                if (list != null) {
                    productsList.value = list.products!!
                }
            }
        }else{
            Log.e("selectCategories api","hata")
        }
    }
}