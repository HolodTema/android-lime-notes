package com.terabyte.realmnotes.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val isDarkTheme: Flow<Boolean>

    suspend fun saveIsDarkTheme(isDarkTheme: Boolean)
}