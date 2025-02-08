package iut.nantes.project.stores.exception

import org.springframework.http.converter.HttpMessageNotReadableException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.beans.TypeMismatchException
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.message ?: "Requête invalide")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.map { it.defaultMessage ?: "Erreur inconnue" }
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), errors.joinToString(", "))
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundException(ex: NoSuchElementException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.message ?: "Ressource non trouvée")
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }
    @ExceptionHandler(TypeMismatchException::class)
    fun handleTypeMismatchException(ex: TypeMismatchException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "ID invalide. Veuillez fournir un identifiant valide."
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val cause = ex.cause
        if (cause is MissingKotlinParameterException) {
            val fieldName = cause.path.joinToString(".") { it.fieldName ?: "inconnu" }
            val errorMessage = "Le champ '$fieldName' est obligatoire"
            val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage)
            return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
        }
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Requête illisible")
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ErrorResponse> {
        ex.printStackTrace()
        println("Exception générique capturée: ${ex.message}")
        val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Une erreur inattendue est survenue")
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        println("Exception capturée : ${ex.message}")
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Ressource non trouvée")
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }


}

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)
