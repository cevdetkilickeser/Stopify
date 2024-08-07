package com.cevdetkilickeser.stopify.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.model.single_genre.SingleGenreData
import com.cevdetkilickeser.stopify.repo.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMSingleGenre @Inject constructor(private val repository: ServiceRepository) : ViewModel() {
    private val _state = MutableStateFlow<List<SingleGenreData>>(emptyList())
    val state: StateFlow<List<SingleGenreData>> = _state


    fun getSingleGenreDataList(genreId: String) {
        viewModelScope.launch {
            try {
                val singleGenreDataList = repository.getSingleGenreDataList(genreId)
                _state.value = singleGenreDataList
            } catch (e: Exception) {
                Log.e("ŞŞŞ", "Hata")
            }
        }
    }
}