package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.repo.LikeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VMLikes @Inject constructor(private val likeRepository: LikeRepository) : ViewModel() {

    private val _likeListState = MutableStateFlow<List<Like>>(emptyList())
    val likeListState: StateFlow<List<Like>> = _likeListState

    fun getLikes(userId: String) {
        viewModelScope.launch {
            _likeListState.value = likeRepository.getLikes(userId)
        }
    }

    fun insertLike(like: Like) {
        viewModelScope.launch {
            likeRepository.insertLike(like)
            getLikes(like.userId)
        }
    }

    fun deleteLike(like: Like) {
        viewModelScope.launch {
            likeRepository.deleteLike(like)
            getLikes(like.userId)
        }
    }
}