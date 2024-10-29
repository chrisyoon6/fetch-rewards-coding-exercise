package com.fetch.fetchapp.repository

import com.fetch.fetchapp.data.ListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.net.URL

/**
 * Manages the data used in this app
 */
class Repository {

    private var _listItems = MutableStateFlow<List<ListItem>>(emptyList())
    val listItems = _listItems.asStateFlow()

    init {
        updateListItems()
    }

    /**
     * Updates the dataset
     */
    private fun updateListItems() {
        CoroutineScope(Dispatchers.Default).launch {
            _listItems.value = processItems(fetchData())
        }
    }

    /**
     * Returns a list of [ListItem], by processing the [ListItem.DataItem] items in a format to be
     * displayed.
     * Items are processed by grouping them based on the list IDs (indicated by adding
     * [ListItem.HeaderItem] at the start of each group) and items within each group is
     * sorted based on the name.
     */
    private fun processItems(dataItems: List<ListItem.DataItem>): List<ListItem> {
        val processedItems = mutableListOf<ListItem>()
        // group items based on the list id and sorted
        dataItems.groupBy { it.listId }.toSortedMap().forEach { (listId, dataItems) ->
            // add headers to the start of each group showing the corresponding list id
            processedItems.add(ListItem.HeaderItem(listId))
            // then sort the items within this group based on the name, in lexicographical order
            val sortedDataItems = dataItems.sortedWith(compareBy { it.name })
            processedItems.addAll(sortedDataItems)
        }
        return processedItems
    }

    /**
     * Performs a network call to fetch data.
     * Returns a list of [ListItem.DataItem], with any items with blank or null values for "name"
     * is filtered.
     */
    private suspend fun fetchData(): List<ListItem.DataItem> {
        val jsonStr = withContext(Dispatchers.IO) { URL(ROOT_URL).readText() }
        // replaces null values in "name" with its default value, which is an empty string so it can be filtered accordingly
        val json = Json {coerceInputValues = true}
        val res = json.decodeFromString<List<ListItem.DataItem>>(jsonStr)
        // only have elements with non empty "name"s
        return res.filter { it.name.isNotBlank() }
    }

    companion object {
        private const val ROOT_URL: String = "https://fetch-hiring.s3.amazonaws.com/hiring.json"
    }
}
