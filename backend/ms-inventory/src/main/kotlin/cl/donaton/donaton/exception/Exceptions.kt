package cl.donaton.donaton.exception
 
class NotFoundException(message: String) : RuntimeException(message)
class BadRequestException(message: String) : RuntimeException(message)
class InsufficientStockException(message: String) : RuntimeException(message)
class GenericException(message: String) : RuntimeException(message)
