package cl.donaton.donaton.model
 
import jakarta.persistence.*
 
@Entity
@Table(name = "collection_centers")
data class CollectionCenter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
 
    @Column(nullable = false)
    val name: String,
 
    @Column(nullable = false)
    val address: String,
 
    @Column(nullable = false)
    val region: String,
 
    @Column(nullable = false)
    val capacity: Int,
 
    @Column(nullable = false)
    val active: Boolean = true
) {
    /* Constructor vacío requerido por JPA */
    constructor() : this(0, "", "", "", 0, true)
}
