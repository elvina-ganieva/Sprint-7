package com.example.pleasework.security.service

import com.example.pleasework.security.model.MyUser
import com.example.pleasework.security.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {
    @Autowired
    lateinit var dao: UserRepository

    override fun loadUserByUsername(p0: String): UserDetails {
        val myUser: MyUser = dao.findByLogin(p0) ?: throw UsernameNotFoundException("User not found")
        val user = User.builder()
            .username(myUser.login)
            .password(myUser.password)
        if (myUser.role != null)
            user.roles(myUser.role)
        else
            user.roles()
        return user.build()
    }
}