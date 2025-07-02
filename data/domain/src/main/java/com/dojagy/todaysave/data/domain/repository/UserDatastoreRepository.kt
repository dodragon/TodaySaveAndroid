package com.dojagy.todaysave.data.domain.repository

import com.dojagy.todaysave.data.model.user.UserModel
import kotlinx.coroutines.flow.Flow

interface UserDatastoreRepository {

    val user: Flow<UserModel>

    suspend fun setUser(
        data: UserModel
    )
}