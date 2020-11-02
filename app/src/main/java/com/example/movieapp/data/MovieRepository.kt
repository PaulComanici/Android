package com.example.movieapp.data

import android.util.Log
import com.example.movieapp.core.TAG


object MovieRepository {
    private var cachedItems: MutableList<Movie>? = null;

    suspend fun loadAll(): List<Movie> {
        Log.i(TAG, "loadAll")
        if (cachedItems != null) {
            return cachedItems as List<Movie>;
        }
        cachedItems = mutableListOf()
        val items = ItemApi.service.find()
        cachedItems?.addAll(items)
        return cachedItems as List<Movie>
    }

    suspend fun load(itemId: String): Movie {
        Log.i(TAG, "load")
        val item = cachedItems?.find { it.id == itemId }
        if (item != null) {
            return item
        }
        return ItemApi.service.read(itemId)
    }

    suspend fun save(item: Movie): Movie {
        Log.i(TAG, "save")
        val createdItem = ItemApi.service.create(item)
        cachedItems?.add(createdItem)
        return createdItem
    }

    suspend fun update(item: Movie): Movie {
        Log.i(TAG, "update")
        val updatedItem = ItemApi.service.update(item.id, item)
        val index = cachedItems?.indexOfFirst { it.id == item.id }
        if (index != null) {
            cachedItems?.set(index, updatedItem)
        }
        return updatedItem
    }
}