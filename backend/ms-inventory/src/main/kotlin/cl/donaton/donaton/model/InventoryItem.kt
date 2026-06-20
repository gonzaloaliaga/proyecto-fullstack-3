package cl.donaton.donaton.model
 
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "inventory_items")
data class InventoryItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_center_id", nullable = false)
    val collectionCenter: CollectionCenter,
 
    @Column(nullable = false)
    val resource: String,
 
    @Column(nullable = false)
    val quantity: Int,
 
    @Column(nullable = false)
    val unit: String,
 
    @Column(name = "last_updated", nullable = false)
    val lastUpdated: LocalDateTime = LocalDateTime.now()
) {
    /* Constructor vacío requerido por JPA */
    constructor() : this(0, CollectionCenter(), "", 0, "", LocalDateTime.now())
}
