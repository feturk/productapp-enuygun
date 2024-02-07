package com.feyzaeda.productapp.models

interface ListFragmentCommunicator {
    fun goToDetails(productId: Int)
    fun addToFavorites(product: Products)
    fun deleteToFavorites(product: Products)
    fun addToOrders(product: Products)
    fun deleteToOrders(product: Products)
}