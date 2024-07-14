package com.ohforbidden.bugreport.domain.user

import com.ohforbidden.bugreport.domain.user.entity.User

interface UserRepository {
    fun save(user: User): User
    fun findById(userId: Long): User?
}