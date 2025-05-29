package com.example.productaggregator.ui.adapter

import Product
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.productaggregator.R

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.productName)
        private val desc = view.findViewById<TextView>(R.id.productDescription)
        private val stock = view.findViewById<TextView>(R.id.productStock)
        private val price = view.findViewById<TextView>(R.id.productPrice)

        fun bind(product: Product) {
            name.text = product.name
            desc.text = product.description
            stock.text = "Stock: ${product.stock}"
            price.text = "Price: ₹${String.format("%.2f", product.price)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
        }
    }
}




