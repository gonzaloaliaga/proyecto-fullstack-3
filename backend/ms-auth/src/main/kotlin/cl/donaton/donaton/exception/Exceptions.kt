package cl.donaton.donaton.exception

class UnauthorizedException(message: String) : RuntimeException(message)
class NotFoundException(message: String) : RuntimeException(message)
class BadCredentialsException(message: String) : RuntimeException(message)
class GenericException(message: String) : RuntimeException(message)