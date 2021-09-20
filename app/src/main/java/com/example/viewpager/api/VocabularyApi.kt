package com.example.viewpager.api

import com.example.viewpager.ui.navigation.model.Vocabulary
import retrofit2.http.GET

interface VocabularyApi {

    @GET("/randomwords")
    suspend fun getRandomWords(): Vocabulary
}