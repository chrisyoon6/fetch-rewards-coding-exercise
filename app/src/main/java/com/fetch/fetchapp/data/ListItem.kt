package com.fetch.fetchapp.data

/**
 * Represents the items to be displayed to the list
 */
sealed class ListItem {
    data class DataItem(val id: Int, val listId: Int, val name: String) : ListItem()
    data class HeaderItem(val listId: Int) : ListItem()
}
