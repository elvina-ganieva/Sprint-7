import dao.TeamDAO
import entity.Team
import entity.Player
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Player::class.java)
        .addAnnotatedClass(Team::class.java)
        .buildSessionFactory()

    sessionFactory.use { sf ->
        val teamDao = TeamDAO(sf)

        val team1 = Team(name = "Team1")
        team1.addPlayerToTeam(Player(name = "Ivan"))
        team1.addPlayerToTeam(Player(name = "Petr"))

        val team2 = Team(name = "Team2")
        team2.addPlayerToTeam(Player(name = "Sergey"))
        team2.addPlayerToTeam(Player(name = "Oleg"))

        teamDao.save(team1)
        teamDao.save(team2)

        val foundTeam1 = teamDao.find(team1.id)
        println("Найдена команда1: $foundTeam1\n")

        val allTeamsBefore = teamDao.findAll()
        println("Все команды до удаления: $allTeamsBefore\n")

        teamDao.delete(team1.id)

        val allTeamsAfter = teamDao.findAll()
        println("Все команды после удаления: $allTeamsAfter\n")
        
    }
}