package com.ohforbidden.bugreport.domain.user.entity

import com.ohforbidden.global.auth.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
class User (
    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false, unique = true)
    val email: String, // 유니크키 필요

    // DB 저장시 암호화 (encoder 사용)
    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val role: Role,

    val imageUrl: String? = null,
) : com.ohforbidden.bugreport.domain.common.BaseEntity()