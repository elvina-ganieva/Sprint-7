package com.example.springdata.persistence.repository

import com.example.springdata.persistence.entity.Player
import com.example.springdata.persistence.entity.Team
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TeamRepository : CrudRepository<Team, Long> {
    fun findByName(name: String): List<Team>

    @Query("SELECT t from Team t WHERE :name IN (SELECT p.name from Player p where p.team = t)")
    fun findByPlayersContainingName(@Param("name") name: String): List<Team>

}