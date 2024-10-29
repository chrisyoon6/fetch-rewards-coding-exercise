package com.fetch.fetchapp.repository

import com.fetch.fetchapp.data.DataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL

class Repository {
    var _dataItems = MutableStateFlow<List<DataItem>>(emptyList())
    val dataItems = _dataItems.asStateFlow()

    /**
     * Updates the dataset
     */
    private fun updateDataItems() {
        CoroutineScope(Dispatchers.Default).launch {
            _dataItems.value = fetchData()
        }
    }

    /**
     * Returns a list of [DataItem], with any items with blank or null values for "name" is filtered.
     */
    private suspend fun fetchData(): List<DataItem> {
        val jsonStr = withContext(Dispatchers.IO) { URL(ROOT_URL).readText() }
        val jsonArray = JSONArray(jsonStr)
        val res = mutableListOf<DataItem>()
        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            if (jsonObj.isNull("name") || jsonObj.getString("name").isBlank()) {
                continue
            }
            val id: Int = jsonObj.getInt("id")
            val listId: Int = jsonObj.getInt("listId")
            val name: String = jsonObj.getString("name")
            res.add(DataItem(id, listId, name))
        }
        return res.sortedWith(compareBy({it.listId}, {it.name}))
    }

    init {
        updateDataItems()
    }

    companion object {
        private const val ROOT_URL: String = "https://fetch-hiring.s3.amazonaws.com/hiring.json"
    }
}