package com.example.productaggregator.ui.main

import Product
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productaggregator.data.ProductRepository
import com.example.productaggregator.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> get() = _products

    private val productMap = mutableMapOf<Int, Product>()
    private val updatedPriceIds = mutableSetOf<Int>()
    private var tax: Double = 0.0

    fun loadProducts() {
        viewModelScope.launch {
            _products.value = Resource.Loading()

            try {
                withContext(Dispatchers.IO) {
                    val allProducts = repository.getAllProducts()
                    productMap.clear()
                    allProducts.forEach { productMap[it.id] = it }
                }

                _products.value = Resource.Success(productMap.values.toList())

                withContext(Dispatchers.IO) {
                    coroutineScope {
                        launch { fetchAndApplyTax() }
                        launch { fetchAndDeleteProducts() }
                        launch { fetchAndAddNewProducts() }
                        launch { fetchAndUpdateStocks() }
                        launch { fetchAndUpdatePrices() }
                    }
                }
            } catch (e: Exception) {
                _products.value = Resource.Error("${e.message} - Failed to load products")
            }
        }
    }

    private suspend fun fetchAndApplyTax() {
        tax = repository.getPriceTax()
        productMap.forEach { (_, product) ->
            if (product.id !in updatedPriceIds) {
                product.price = product.price?.plus(product.price!! * tax / 100)
            }
        }
        _products.postValue(Resource.Success(productMap.values.toList()))
    }

    private suspend fun fetchAndDeleteProducts() {
        val idsToDelete = repository.getProductsToDelete()
        idsToDelete.forEach { productMap.remove(it) }
        _products.postValue(Resource.Success(productMap.values.toList()))
    }

    private suspend fun fetchAndAddNewProducts() {
        val newProducts = repository.getNewProducts()
        newProducts.forEach {
            it.price = it.price?.plus(it.price!! * tax / 100)
            productMap[it.id] = it
        }
        _products.postValue(Resource.Success(productMap.values.toList()))
    }

    private suspend fun fetchAndUpdateStocks() {
        val updatedStocks = repository.getUpdatedStocks()
        updatedStocks.forEach { (id, stock) ->
            productMap[id]?.stock = stock
        }
        _products.postValue(Resource.Success(productMap.values.toList()))
    }

    private suspend fun fetchAndUpdatePrices() {
        val updatedPrices = repository.getUpdatedPrices()
        updatedPrices.forEach { (id, price) ->
            productMap[id]?.let {
                it.price = price
                updatedPriceIds.add(id)
            }
        }
        _products.postValue(Resource.Success(productMap.values.toList()))
    }
}
