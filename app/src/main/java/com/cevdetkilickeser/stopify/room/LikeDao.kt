package com.cevdetkilickeser.stopify.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.stopify.data.entity.Like
@Dao
interface LikeDao {

    @Insert
    suspend fun insertLike(like: Like)

    @Delete
    suspend fun deleteLike(like: Like)

    @Query("DELETE FROM like_table WHERE userId = :userId AND trackId = :trackId")
    suspend fun deleteLikeByTrackId(userId: String, trackId: String)

    @Query("SELECT * FROM like_table WHERE userId = :userId")
    suspend fun getLikes(userId: String): List<Like>
}