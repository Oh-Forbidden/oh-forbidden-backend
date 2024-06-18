package com.ohforbidden.domain.user

import com.ohforbidden.global.auth.Role
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long,
    val email: String, // 유니크키 필요

    // DB 저장시 암호화 (encoder 사용)
    val password: String = UUID.randomUUID().toString().replace("-", "").substring(0, 10),

    val nickname: String,
    val role: Role
)