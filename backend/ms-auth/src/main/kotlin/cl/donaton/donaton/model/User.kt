package cl.donaton.donaton.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var username: String,
    val password: String,
) {
    /* Constructor vacío requerido por JPA */
    constructor() : this(0, "", "")
}