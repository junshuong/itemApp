package org.wit.item.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.item.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*
import timber.log.Timber.i

const val JSON_FILE = "items.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<ItemModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ItemJSONStore(private val context: Context) : ItemStore {

    var items = mutableListOf<ItemModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ItemModel> {
        logAll()
        return items
    }

    override fun create(item: ItemModel) {
        item.id = generateRandomId()
        items.add(item)
        i(" Item added")
        serialize()
    }


    override fun update(item: ItemModel) {
        val itemsList = findAll() as ArrayList<ItemModel>
        var foundItem: ItemModel? = itemsList.find { i -> i.id == item.id }
        if (foundItem != null) {
            foundItem.name = item.name
            foundItem.description = item.description
            foundItem.quantity = item.quantity
            foundItem.location = item.location
            foundItem.image = item.image
            foundItem.lat = item.lat
            foundItem.lng = item.lng
            foundItem.zoom = item.zoom
        }
        serialize()
    }

    override fun delete(item: ItemModel) {
        items.remove(item)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(items, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        items = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        items.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}