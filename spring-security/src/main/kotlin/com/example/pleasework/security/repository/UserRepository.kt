package com.example.pleasework.security.repository

import com.example.pleasework.security.model.MyUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<MyUser, Long> {
    fun findByLogin(login: String): MyUser?
}