package com.fetch.fetchapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fetch.fetchapp.R
import com.fetch.fetchapp.data.ListItem
import java.util.Locale

/**
 * An adapter to manage the RecyclerView
 */
class ItemAdapter(private var items: List<ListItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val HEADER_ITEM_VIEW_TYPE = 0
        const val DATA_ITEM_VIEW_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER_ITEM_VIEW_TYPE) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_category, parent, false)
            return HeaderItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_body, parent, false)
            return DataItemViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val listItem: ListItem = items[position]
        return if (listItem is ListItem.HeaderItem) {
            HEADER_ITEM_VIEW_TYPE
        } else {
            DATA_ITEM_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem: ListItem = items[position]
        if (holder is HeaderItemViewHolder) {
            val headerItem = listItem as ListItem.HeaderItem
            holder.bind(headerItem)
        } else {
            val dataItem = listItem as ListItem.DataItem
            (holder as DataItemViewHolder).bind(dataItem)
        }
    }

    /**
     * Updates the data displayed with [newItems]
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateDataset(newItems: List<ListItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}

/**
 * ViewHolder for header items
 */
class HeaderItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var headerTitle: TextView = itemView.findViewById(R.id.header_title)

    /**
     * Binds the header view with the appropriate [headerItem]
     */
    fun bind(headerItem: ListItem.HeaderItem) {
        headerTitle.text = String.format(Locale.ENGLISH, "List ID: %d", headerItem.listId)
    }
}

/**
 * ViewHolder for body/data items
 */
class DataItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var itemName: TextView = itemView.findViewById(R.id.item_name)
    private var itemListId: TextView = itemView.findViewById(R.id.item_listId)
    private var itemId: TextView = itemView.findViewById(R.id.item_id)

    /**
     * Binds the body view with the appropriate [dataItem]
     */
    fun bind(dataItem: ListItem.DataItem) {
        itemName.text = String.format(Locale.ENGLISH, "name: %s", dataItem.name)
        itemListId.text = String.format(Locale.ENGLISH, "listId: %d", dataItem.listId)
        itemId.text = String.format(Locale.ENGLISH, "id: %d", dataItem.id)
    }
}
