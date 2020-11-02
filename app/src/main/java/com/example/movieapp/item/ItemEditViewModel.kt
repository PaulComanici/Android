package com.example.movieapp.item

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.core.TAG
import com.example.movieapp.data.Movie
import com.example.movieapp.data.MovieRepository
import kotlinx.coroutines.launch


class ItemEditViewModel : ViewModel() {
    private val mutableItem = MutableLiveData<Movie>().apply { value = Movie("", "", "", "", "") }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val item: LiveData<Movie> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadItem...")
            mutableFetching.value = true
            mutableException.value = null
            try {
                mutableItem.value = MovieRepository.load(itemId)
                Log.i(TAG, "loadItem succeeded")
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "loadItem failed", e)
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }

    fun saveOrUpdateItem(nume: String, regizor: String, releasedate: String, released : String) {
        viewModelScope.launch {
            Log.i(TAG, "saveOrUpdateItem...");
            val item = mutableItem.value ?: return@launch
            item.nume = nume
            item.regizor = regizor
            item.releasedate = releasedate
            item.released = released
            mutableFetching.value = true
            mutableException.value = null
            try {
                if (item.id.isNotEmpty()) {
                    mutableItem.value = MovieRepository.update(item)
                } else {
                    mutableItem.value = MovieRepository.save(item)
                }
                Log.i(TAG, "saveOrUpdateItem succeeded");
                mutableCompleted.value = true
                mutableFetching.value = false
            } catch (e: Exception) {
                Log.w(TAG, "saveOrUpdateItem failed", e);
                mutableException.value = e
                mutableFetching.value = false
            }
        }
    }
}