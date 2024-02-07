package com.feyzaeda.productapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.feyzaeda.productapp.databinding.ItemProductsFavBinding
import com.feyzaeda.productapp.models.ProductsRoomDbFavEntity
import com.feyzaeda.productapp.util.glide
import com.feyzaeda.productapp.viewmodels.RoomDbViewModel

class FavoriteProductsAdapter(
    private var productsList: MutableList<ProductsRoomDbFavEntity>,
    var context: Context,
    private var viewModel: RoomDbViewModel,
    private val onItemClick: (Int) -> Unit
) :
    RecyclerView.Adapter<FavoriteProductsAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemProductsFavBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemProductsFavBinding

        fun bind(productId: Int) {
            itemView.setOnClickListener {
                onItemClick(productId)
            }
        }

        init {
            this.binding = binding
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        productsList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val tasarim = ItemProductsFavBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(tasarim)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = productsList[position]

        val t = holder.binding
        t.txtName.text = product.title
        t.txtBrand.text = product.description
        t.txtPrice.text = product.price.toString()
        t.productImageView.glide(context, product.images[0])

        t.imageViewEmpty.visibility = View.INVISIBLE
        t.imageViewFull.visibility = View.VISIBLE

        holder.bind(product.id!!)
        t.imageViewFull.setOnClickListener {
            viewModel.deleteFavProducts(product)
            Toast.makeText(context, product.title + " Favorilerden Silindi", Toast.LENGTH_SHORT)
                .show()
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}