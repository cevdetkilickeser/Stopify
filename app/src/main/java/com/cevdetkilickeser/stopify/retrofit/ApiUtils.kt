package com.cevdetkilickeser.stopify.retrofit

class ApiUtils {
    companion object {
        private const val BASE_URL = "https://api.deezer.com/"

        fun getApiService(): ApiService {
            return RetrofitClient.getClient(BASE_URL).create(ApiService::class.java)
        }
    }
}