package com.example.pleasework.security.model

import javax.persistence.*

@Entity
class MyUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column
    var login: String,
    @Column
    var password: String,
    @Column
    var role: String?
)
