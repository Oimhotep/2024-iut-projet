package iut.nantes.project.products.errors

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


class InvalidArgumentException(message : String ) : RuntimeException(message)
class ConflictException(message : String) : RuntimeException(message)
class NotFoundException(message : String) : RuntimeException(message)
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ConflictException::class)
    fun handleConflit(ex : ConflictException): ResponseEntity<String>{
        return ResponseEntity.status( HttpStatus.CONFLICT).body(ex.message)
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex : NotFoundException) : ResponseEntity<String>{
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
    }

    @ExceptionHandler(InvalidArgumentException::class)
    fun handleInvalidArgument(ex : IllegalArgumentException) : ResponseEntity<String>{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleInvalidUUID(ex : MethodArgumentTypeMismatchException) : ResponseEntity<String>{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format")
    }
}