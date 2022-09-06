package org.wit.item.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.wit.item.databinding.CardItemBinding
import org.wit.item.models.ItemModel

interface ItemListener {
    fun onItemClick(item: ItemModel)
}

class ItemAdapter constructor(private var items: List<ItemModel>,
                              private val listener: ItemListener) :
    RecyclerView.Adapter<ItemAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val item = items[holder.adapterPosition]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = items.size

    class MainHolder(private val binding : CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemModel, listener: ItemListener) {
            binding.itemName.text = item.name
            binding.description.text = item.description
            binding.itemQuantity.text = item.quantity.toString()
            Picasso.get().load(item.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onItemClick(item) }
        }
    }
}