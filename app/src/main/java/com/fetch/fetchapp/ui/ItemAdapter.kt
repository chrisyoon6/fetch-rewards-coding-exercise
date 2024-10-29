package com.fetch.fetchapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fetch.fetchapp.R
import com.fetch.fetchapp.data.DataItem
import java.util.Locale

class ItemAdapter(private var items: List<DataItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val dataItem: DataItem = items[position]
        (holder as ItemViewHolder).bind(dataItem)
    }

    /**
     * Updates the data displayed with [newItems]
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(newItems: List<DataItem>) {
        items = newItems
        notifyDataSetChanged()
    }

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var itemName: TextView = itemView.findViewById(R.id.item_name)
    private var itemListId: TextView = itemView.findViewById(R.id.item_listId)
    private var itemId: TextView = itemView.findViewById(R.id.item_id)

    /**
     * Binds the view with the appropriate [dataItem]
     */
    fun bind(dataItem: DataItem) {
        itemName.text = String.format(Locale.ENGLISH, "name: %s", dataItem.name)
        itemListId.text = String.format(Locale.ENGLISH, "listId: %d", dataItem.listId)
        itemId.text = String.format(Locale.ENGLISH, "id: %d", dataItem.id)
    }
}