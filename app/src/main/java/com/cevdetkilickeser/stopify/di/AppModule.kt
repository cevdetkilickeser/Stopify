package com.cevdetkilickeser.stopify.di

import android.content.Context
import com.cevdetkilickeser.stopify.retrofit.ApiService
import com.cevdetkilickeser.stopify.retrofit.ApiUtils
import com.cevdetkilickeser.stopify.room.AppDatabase
import com.cevdetkilickeser.stopify.room.DownloadDao
import com.cevdetkilickeser.stopify.room.HistoryDao
import com.cevdetkilickeser.stopify.room.LikeDao
import com.cevdetkilickeser.stopify.room.UserPlaylistDao
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiUtils.getApiService()
    }

    @Provides
    @Singleton
    fun provideHistoryDao(@ApplicationContext context: Context): HistoryDao {
        return AppDatabase.getDatabase(context).getHistoryDao()
    }

    @Provides
    @Singleton
    fun provideLikeDao(@ApplicationContext context: Context): LikeDao {
        return AppDatabase.getDatabase(context).getLikeDao()
    }

    @Provides
    @Singleton
    fun provideDownloadDao(@ApplicationContext context: Context): DownloadDao {
        return AppDatabase.getDatabase(context).getDownloadDao()
    }

    @Provides
    @Singleton
    fun providePlayerTrackDao(@ApplicationContext context: Context): UserPlaylistDao {
        return AppDatabase.getDatabase(context).getUserPlaylistDao()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}