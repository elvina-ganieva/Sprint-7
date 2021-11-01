package dao

import entity.Team
import org.hibernate.SessionFactory

class TeamDAO(
    private val sessionFactory: SessionFactory
) {
    fun save(Team: Team) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.persist(Team)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Team? {
        val result: Team?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Team::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun findAll(): List<Team> {
        val result: List<Team>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Team").list() as List<Team>
            session.transaction.commit()
        }
        return result
    }

    fun delete(id: Long) {
        val team: Team?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            team = session.get(Team::class.java, id)
            session.delete(team)
            session.transaction.commit()
        }
        return
    }
}