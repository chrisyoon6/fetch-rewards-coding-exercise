package com.fetch.fetchapp.repository

import com.fetch.fetchapp.data.ListItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class Repository {
    var _listItems = MutableStateFlow<List<ListItem>>(emptyList())
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
        // group items based on the list id
        dataItems.groupBy { it.listId }.forEach { (listId, dataItems) ->
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
        val jsonArray = JSONArray(jsonStr)
        val res = mutableListOf<ListItem.DataItem>()
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            if (jsonObj.isNull("name") || jsonObj.getString("name").isBlank()) {
                continue
            }
            val id: Int = jsonObj.getInt("id")
            val listId: Int = jsonObj.getInt("listId")
            val name: String = jsonObj.getString("name")
            res.add(ListItem.DataItem(id, listId, name))
        }
        return res
    }

    companion object {
        private const val ROOT_URL: String = "https://fetch-hiring.s3.amazonaws.com/hiring.json"
    }
}
