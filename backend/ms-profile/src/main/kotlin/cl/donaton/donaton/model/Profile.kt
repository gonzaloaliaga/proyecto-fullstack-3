package cl.donaton.donaton.model

import jakarta.persistence.*

@Entity
@Table(name = "profiles")
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val role: String,
    val email: String,
    val address: String,
    val run: String
) {
    // Constructor vacío requerido por JPA
    constructor() : this(0, "", "", "", "")
}