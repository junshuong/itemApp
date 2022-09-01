package org.wit.item.models

interface ItemStore {
    fun findAll(): List<ItemModel>
    fun create(item: ItemModel)
    fun update(item: ItemModel)
    fun delete(item: ItemModel)
}