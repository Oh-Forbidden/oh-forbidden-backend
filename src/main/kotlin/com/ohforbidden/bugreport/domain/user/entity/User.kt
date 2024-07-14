package com.ohforbidden.bugreport.domain.user.entity

import com.ohforbidden.bugreport.domain.common.BaseEntity
import com.ohforbidden.bugreport.global.auth.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User (
    @Column(nullable = false, length = 100)
    val nickname: String,

    @Column(nullable = false, unique = true, length = 100)
    val email: String, // 유니크키 필요

    // DB 저장시 암호화 (encoder 사용)
    @Column(nullable = false, length = 100)
    val password: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role,

    @Column(length = 500)
    val imageUrl: String? = null,
) : BaseEntity() {
}