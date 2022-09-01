package org.wit.item.main

import android.app.Application
import org.wit.item.models.ItemJSONStore
import org.wit.item.models.ItemMemStore
import org.wit.item.models.ItemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp: Application(){

    lateinit var items: ItemStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        items = ItemJSONStore(applicationContext)
        i("Item App started")
    }
}