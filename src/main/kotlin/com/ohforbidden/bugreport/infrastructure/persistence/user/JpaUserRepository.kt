package com.ohforbidden.bugreport.infrastructure.persistence.user

import com.ohforbidden.bugreport.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository : JpaRepository<User, Long>