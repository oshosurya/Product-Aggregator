package com.example.productaggregator.data

import com.example.producthandling.StreamLibAPI


class ProductRepository (private val api: StreamLibAPI) {
    suspend fun getAllProducts() = api.getAllProducts()
    suspend fun getPriceTax() = api.getPriceTax()
    suspend fun getUpdatedPrices() = api.getCompanyUpdatedPrices()
    suspend fun getUpdatedStocks() = api.getCompanyUpdatedStocks()
    suspend fun getProductsToDelete() = api.getProductsToDelete()
    suspend fun getNewProducts() = api.getNewProducts()
}