package com.ohforbidden.bugreport.domain.common

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
    // id는 DB에서 생성되므로 0으로 초기화, 이후 DB에서 재생성해줌 -> val 사용
    // 하지만 @CreateDate, LastModifiedDate는 영속화 시점에 어플리케이션에서 생성 -> var 사용 (val는 에러 발생)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L // auto increment로 PK 지정

    @Column(nullable = true, updatable = false)
    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.MIN
        protected set

    @Column(nullable = true, updatable = true)
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.MIN
        protected set

//    @Transient
//    private var _isNew = true

    override fun getId(): Long = id
    override fun isNew(): Boolean = id == 0L
    override fun hashCode(): Int = Objects.hashCode(id)

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }

        if (this::class != other::class) {
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