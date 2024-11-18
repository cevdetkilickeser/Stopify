package com.cevdetkilickeser.stopify.repo

import com.cevdetkilickeser.stopify.data.entity.Like
import com.cevdetkilickeser.stopify.room.LikeDao
import javax.inject.Inject

class LikeRepository @Inject constructor(private val likeDao: LikeDao) {

    suspend fun insertLike(like: Like) = likeDao.insertLike(like)

    suspend fun deleteLikeByTrackId(userId: String, trackId: String) = likeDao.deleteLikeByTrackId(userId, trackId)

    suspend fun getLikes(userId: String) = likeDao.getLikes(userId)
}