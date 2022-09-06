package org.wit.item.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.item.R
import org.wit.item.databinding.ActivityItemBinding
import org.wit.item.helpers.showImagePicker
import org.wit.item.main.MainApp
import org.wit.item.models.Location
import org.wit.item.models.ItemModel
import timber.log.Timber.i

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    var item = ItemModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false
//    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        i("Item Activity has started ...")

        if (intent.hasExtra("item_edit")) {
            edit = true
            item = intent.extras?.getParcelable("item_edit")!!
            binding.itemName.setText(item.name)
            binding.description.setText(item.description)
            binding.itemQuantity.setText(item.quantity.toString()) // change variable class from Int to String
            binding.itemFoundLocation.setText(item.location)
            binding.btnAdd.setText(R.string.save_item)
            Picasso.get()
                .load(item.image)
                .into(binding.itemImage)
            if (item.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_item_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            item.name = binding.itemName.text.toString()
            item.description = binding.description.text.toString()
            item.quantity = binding.itemQuantity.text.toString().toInt() // changed binding back to Int (from String)
            item.location = binding.itemFoundLocation.text.toString()
            if(item.name.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_itemName, Snackbar.LENGTH_LONG)
                    .show()
            }
            else {
                if(edit) {
                    app.items.update(item.copy())
                } else {
                    app.items.create(item.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
            i("Select image")
        }

        binding.itemLocation.setOnClickListener {
            val location =
                org.wit.item.models.Location(52.245696, -7.139102, 15f) // SETU Waterford Campus
            if (item.zoom != 0f) {
                location.lat = item.lat
                location.lng = item.lng
                location.zoom = item.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
            i ("Set Location Pressed")
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            item.image = result.data!!.data!!
                            Picasso.get()
                                .load(item.image)
                                .into(binding.itemImage)
                            binding.chooseImage.setText(R.string.change_item_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location     >("location")!!
                            i("Location == $location")
                            item.lat = location.lat
                            item.lng = location.lng
                            item.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}

