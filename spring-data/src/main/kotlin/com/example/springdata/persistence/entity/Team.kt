package com.example.springdata.persistence.entity

import javax.persistence.*

@Entity
class Team(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column
    var name: String,

    @OneToMany(
        cascade = [CascadeType.ALL],
        mappedBy = "team",
        fetch = FetchType.EAGER
    )
    var players: MutableList<Player> = mutableListOf()
) {

    fun addPlayerToTeam(player: Player) {
        players.add(player)
        player.team = this
    }

    override fun toString(): String {
        return "Team(id=$id, name='$name', players=$players)"
    }
}
