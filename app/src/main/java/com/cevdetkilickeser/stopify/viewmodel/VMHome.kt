package com.cevdetkilickeser.stopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.genre.GenreData
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMHome @Inject constructor(private val repository: ServiceRepository) : ViewModel() {
    private val _state = MutableStateFlow<List<GenreData>>(emptyList())
    val state: StateFlow<List<GenreData>> = _state

    init {
        getGenreDataList()
    }

    private fun getGenreDataList() {
        viewModelScope.launch {
            try {
                val genreDataList = repository.getGenreDataList()
                _state.value = genreDataList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }
}