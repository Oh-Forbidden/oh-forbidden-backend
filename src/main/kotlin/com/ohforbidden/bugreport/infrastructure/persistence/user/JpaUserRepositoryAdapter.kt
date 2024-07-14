package com.ohforbidden.bugreport.infrastructure.persistence.user

import com.ohforbidden.bugreport.domain.user.UserRepository
import com.ohforbidden.bugreport.domain.user.entity.User
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class JpaUserRepositoryAdapter(
    private val repository: JpaUserRepository
) : UserRepository {
    override fun save(user: User): User {
        return repository.save(user)
    }

    override fun findById(userId: Long): User? {
        return repository.findById(userId).getOrNull()
    }
}