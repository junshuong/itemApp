package org.wit.item.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.item.R
import org.wit.item.adapters.ItemAdapter
import org.wit.item.adapters.ItemListener
import org.wit.item.databinding.ActivityItemListBinding

import org.wit.item.main.MainApp
import org.wit.item.models.ItemModel

class ItemListActivity : AppCompatActivity(), ItemListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityItemListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadItems()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ItemActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(item: ItemModel) {
        val launcherIntent = Intent (this, ItemActivity::class.java)
        launcherIntent.putExtra("item_edit", item)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadItems() }
    }

    private fun loadItems() {
        showItems(app.items.findAll())
    }

    fun showItems (items: List<ItemModel>) {
        binding.recyclerView.adapter = ItemAdapter(items, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}



