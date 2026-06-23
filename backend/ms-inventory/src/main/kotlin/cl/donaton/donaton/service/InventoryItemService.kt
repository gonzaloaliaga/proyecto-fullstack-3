package cl.donaton.donaton.service
 
import cl.donaton.donaton.dto.AdjustStockDto
import cl.donaton.donaton.dto.CreateInventoryItemDto
import cl.donaton.donaton.exception.InsufficientStockException
import cl.donaton.donaton.exception.NotFoundException
import cl.donaton.donaton.model.InventoryItem
import cl.donaton.donaton.repository.CollectionCenterRepository
import cl.donaton.donaton.repository.InventoryItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
 
@Service
class InventoryItemService(
    private val inventoryItemRepository: InventoryItemRepository,
    private val collectionCenterRepository: CollectionCenterRepository
) {
 
    @Transactional
    fun createInventoryItem(dto: CreateInventoryItemDto): InventoryItem {
        val center = collectionCenterRepository.findByIdOrNull(dto.collectionCenterId)
            ?: throw NotFoundException("Centro de acopio no encontrado")
 
        val item = InventoryItem(
            collectionCenter = center,
            resource = dto.resource,
            quantity = dto.quantity,
            unit = dto.unit,
            lastUpdated = LocalDateTime.now()
        )
        return inventoryItemRepository.save(item)
    }
 
    @Transactional(readOnly = true)
    fun getInventoryItemById(id: Long): InventoryItem {
        return inventoryItemRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Item de inventario no encontrado")
    }
 
    @Transactional(readOnly = true)
    fun getAllInventoryItems(): List<InventoryItem> {
        return inventoryItemRepository.findAll()
    }
 
    @Transactional(readOnly = true)
    fun getInventoryItemsByCollectionCenter(collectionCenterId: Long): List<InventoryItem> {
        return inventoryItemRepository.findByCollectionCenterId(collectionCenterId)
    }
 
    @Transactional
    fun adjustStock(id: Long, dto: AdjustStockDto): InventoryItem {
        val existing = inventoryItemRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Item de inventario no encontrado")
 
        val newQuantity = existing.quantity + dto.quantityChange
        if (newQuantity < 0) {
            throw InsufficientStockException(
                "Stock insuficiente: disponible ${existing.quantity}, se intentó ajustar en ${dto.quantityChange}"
            )
        }
 
        val updated = existing.copy(quantity = newQuantity, lastUpdated = LocalDateTime.now())
        return inventoryItemRepository.save(updated)
    }
 
    @Transactional
    fun deleteInventoryItem(id: Long) {
        val existing = inventoryItemRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Item de inventario no encontrado")
        inventoryItemRepository.delete(existing)
    }
}
