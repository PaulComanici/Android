package com.example.movieapp.items

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.core.TAG
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieRepository
import kotlinx.coroutines.launch

class ItemListViewModel : ViewModel() {
    private val mutableItems = MutableLiveData<List<Movie>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Movie>> = mutableItems
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    fun createItem(position: Int): Unit {
        val list = mutableListOf<Movie>()
        list.addAll(mutableItems.value!!)
        list.add(Movie(position.toString(), "Item " + position, "", "", ""))
        mutableItems.value = list
    }

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "loadItems...");
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutableItems.value = MovieRepository.loadAll()
                Log.d(TAG, "loadItems succeeded");
                mutableLoading.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItems failed", e);
                mutableException.value = e
                mutableLoading.value = false
            }
        }
    }
}