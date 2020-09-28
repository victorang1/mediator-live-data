package com.example.mediatorlivedata

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediatorlivedata.databinding.ItemLayoutBinding
import com.example.mediatorlivedata.db.ItemEntity

class MainAdapter : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {

    private var items: List<ItemEntity> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        var itemBinding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(newItems: List<ItemEntity>) {
        items = newItems
        notifyItemRangeInserted(items.count(), newItems.count())
    }

    override fun getItemCount(): Int = items.count()

    inner class ItemViewHolder(val itemLayoutBinding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(itemLayoutBinding.root) {

        fun bind(item: ItemEntity) {
            itemLayoutBinding.item = item
        }
    }
}