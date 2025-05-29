package com.example.productaggregator.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.productaggregator.R
import com.example.productaggregator.data.ProductRepository
import com.example.productaggregator.ui.adapter.ProductAdapter
import com.example.productaggregator.util.Resource
import com.example.producthandling.StreamLibProvider

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = StreamLibProvider.instance
        val repository = ProductRepository(api)
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        adapter = ProductAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val errorLayout = findViewById<View>(R.id.errorLayout)
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        val retryButton = findViewById<Button>(R.id.retryButton)

        recyclerView.adapter = adapter

        viewModel.products.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    adapter.updateProducts(resource.data!!.toList())
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                    errorMessage.text = resource.message
                }
            }
        }

        retryButton.setOnClickListener { viewModel.loadProducts() }

        viewModel.loadProducts()
    }
}
