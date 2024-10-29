package com.fetch.fetchapp.data

import kotlinx.serialization.Serializable

/**
 * Represents the items to be displayed to the list
 */
sealed class ListItem {
    @Serializable
    data class DataItem(val id: Int, val listId: Int, val name: String = "") : ListItem()
    data class HeaderItem(val listId: Int) : ListItem()
}
