package com.ohforbidden.domain.common

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.Objects

@MappedSuperclass // Entity가 일반 클래스인 BaseEntity를 상속할 수 있도록 설정
@EntityListeners(AuditingEntityListener::class) // 자동 생성 기능 활성화 (생성일, 수정일 등)
abstract class BaseEntity : Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L // auto increment로 PK 지정

    @Column(nullable = false, updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.MIN

    @Column(nullable = false, updatable = true)
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.MIN

//    @Transient
//    private var _isNew = true

    override fun getId(): Long = id
    override fun isNew(): Boolean = if (id == 0L) true else false
    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (other !is HibernateProxy && this::class != other::class) {
            return false
        }

        return id == getIdentifier(other)
    }

    private fun getIdentifier(obj: Any): Any? {
        return if (obj is HibernateProxy) {
            obj.hibernateLazyInitializer.identifier
        } else {
            (obj as BaseEntity).id
        }
    }

//    @PrePersist
//    @PostLoad
//    protected fun load() {
//        _isNew = false
//    }
}