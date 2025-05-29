package com.example.productaggregator.ui.adapter

import Product
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.productaggregator.R

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productList = mutableListOf<Product>()

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.productName)
        private val desc: TextView = view.findViewById(R.id.productDescription)
        private val stock: TextView = view.findViewById(R.id.productStock)
        private val price: TextView = view.findViewById(R.id.productPrice)

        @SuppressLint("SetTextI18n", "DefaultLocale")
        fun bind(product: Product) {
            name.text = product.name
            desc.text = product.description
            stock.text = "Stock: ${product.stock}"
            price.text = "Price: ₹${String.format("%.2f", product.price)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProducts(newProducts: List<Product>) {
        productList.clear()
        productList.addAll(newProducts)
        notifyDataSetChanged()
    }
}




