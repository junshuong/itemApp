package org.wit.item.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ItemMemStore : ItemStore {

    val items = ArrayList<ItemModel>()

    override fun findAll(): List<ItemModel> {
        return items
    }

    override fun create(item: ItemModel) {
        items.add(item)
        logAll()
    }

    override fun update(item: ItemModel) {
        var foundItem: ItemModel? = items.find { i -> i.id == item.id}
        if (foundItem != null) {
            foundItem.name = item.name
            foundItem.description = item.description
            foundItem.quantity = item.quantity
            foundItem.image = item.image
            foundItem.lat = item.lat
            foundItem.lng = item.lng
            foundItem.zoom = item.zoom
            logAll()
        }
    }

    override fun delete(item: ItemModel) {
        items.remove(item)
    }

    private fun logAll() {
        items.forEach { i("$it") }
    }
}