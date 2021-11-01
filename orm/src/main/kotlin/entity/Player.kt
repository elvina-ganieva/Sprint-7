package entity

import javax.persistence.*

@Entity
class Player(
    @Id
    @GeneratedValue
    var id: Long = 0,

    @Column
    var name: String,

    @ManyToOne(
        cascade = [CascadeType.ALL],
        fetch = FetchType.EAGER
    )
    @JoinColumn(name = "team_id")
    var team: Team? = null
) {

    override fun toString(): String {
        return "Player(id=$id, name='$name')"
    }
}
