package com.kantarix.home_service.api.exceptions

import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@ControllerAdvice
class GlobalExceptionHandler {

    private val log = KotlinLogging.logger {  }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        val allErrors = ex.bindingResult.allErrors
        val errorMessage = StringBuilder("")

        for (error in allErrors)
            errorMessage.append(error.defaultMessage).append("\n")

        return ResponseEntity.badRequest().body(errorMessage.toString())
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleValidationException(ex: HttpMessageNotReadableException): ResponseEntity<String> {
        log.debug(ex) { ex.message }
        return ResponseEntity.badRequest().body("Incomplete request body.")
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleValidationException(ex: NoSuchElementException): ResponseEntity<String> {
        log.debug(ex) { ex.message }
        return ResponseEntity.badRequest().body(ex.message)
    }

}