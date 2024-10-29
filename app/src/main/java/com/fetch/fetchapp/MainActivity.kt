package com.fetch.fetchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fetch.fetchapp.repository.Repository
import com.fetch.fetchapp.ui.ItemAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val adapter = ItemAdapter(repository.listItems.value)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repository.listItems.collectLatest { newListItems ->
                // update the RecyclerView with the items
                (recyclerView.adapter as ItemAdapter).updateDataset(newListItems)
            }
        }
    }
}
