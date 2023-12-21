package com.kantarix.home_service.api.exceptions

import com.kantarix.home_service.api.dto.ErrorDto
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.util.ResourceBundle

@ControllerAdvice
class GlobalExceptionHandler(
    @Value("\${spring.messages.basename}")
    private val basename: String,
) {

    private val log = KotlinLogging.logger {  }

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ErrorDto> {
        log.debug(ex) { ex.message }
        return ResponseEntity.status(ex.httpStatus).body(
            ErrorDto(
                code = ex.code,
                messages = listOf(ResourceBundle.getBundle(basename).getString(ex.message))
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorDto> {
        val allErrors = ex.bindingResult.fieldErrors
        val errorMessages = mutableListOf<String>()

        for (error in allErrors)
            errorMessages.add("${error.field.removePrefix("_")} ${error.defaultMessage}")

        return ResponseEntity.badRequest().body(
            ErrorDto(code = "VALIDATION_EXCEPTION", messages = errorMessages)
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorDto> {
        log.debug(ex) { ex.message }
        return ResponseEntity.badRequest().body(
            ErrorDto(code = "INTERNAL_API_EXCEPTION", messages = listOf("Something went wrong."))
        )
    }

}