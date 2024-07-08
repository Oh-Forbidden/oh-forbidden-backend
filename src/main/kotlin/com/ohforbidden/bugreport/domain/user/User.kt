package com.ohforbidden.bugreport.domain.user

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long
)