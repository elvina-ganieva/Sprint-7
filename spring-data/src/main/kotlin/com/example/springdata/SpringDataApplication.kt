package com.example.springdata

import com.example.springdata.persistence.entity.Player
import com.example.springdata.persistence.entity.Team
import com.example.springdata.persistence.repository.TeamRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringDataApplication(
    private val teamRepository: TeamRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val team1 = Team(name = "team")
        team1.addPlayerToTeam(Player(name = "Ivan"))
        team1.addPlayerToTeam(Player(name = "Petr"))

        val team2 = Team(name = "team")
        team2.addPlayerToTeam(Player(name = "Sergey"))
        team2.addPlayerToTeam(Player(name = "Petr"))

        val team3 = Team(name = "anotherTeam")

        team3.addPlayerToTeam(Player(name = "Petr"))
        team3.addPlayerToTeam(Player(name = "Ivan"))

        teamRepository.saveAll(listOf(team1, team2, team3))

        val teamsCalledTeam = teamRepository.findByName("team")
        println(teamsCalledTeam)

        val teamsWithIvan = teamRepository.findByPlayersContainingName("Ivan")
        println(teamsWithIvan)
    }

}

fun main(args: Array<String>) {
    runApplication<SpringDataApplication>(*args)
}
