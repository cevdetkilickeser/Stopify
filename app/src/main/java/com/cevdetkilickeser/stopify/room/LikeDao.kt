package com.cevdetkilickeser.stopify.room

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.stopify.data.entity.Like

interface LikeDao {

    @Insert
    suspend fun insertLike(like: Like)

    @Delete
    suspend fun deleteLike(like: Like)

    @Query("SELECT * FROM like_table WHERE userId = :userId")
    suspend fun getLikes(userId: String): List<Like>
}