package com.mmoney.apps.core.data.repository

import com.mmoney.apps.core.database.dao.UserDao
import com.mmoney.apps.core.database.model.UserEntity
import com.mmoney.apps.core.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    val userProfile: Flow<UserProfile?> = userDao.getUser().map { entity ->
        entity?.let {
            UserProfile(
                id = it.id,
                name = it.name,
                email = it.email,
                avatarUrl = it.avatarUrl
            )
        }
    }

    suspend fun updateUserProfile(userProfile: UserProfile) {
        val entity = UserEntity(
            id = userProfile.id,
            name = userProfile.name,
            email = userProfile.email,
            avatarUrl = userProfile.avatarUrl,
            createdAt = System.currentTimeMillis()
        )
        userDao.insertUser(entity)
    }

    suspend fun createDefaultUser() {
        val defaultUser = UserEntity(
            id = "default_user",
            name = "User",
            email = "user@mmoney.com",
            avatarUrl = null,
            createdAt = System.currentTimeMillis()
        )
        userDao.insertUser(defaultUser)
    }
}