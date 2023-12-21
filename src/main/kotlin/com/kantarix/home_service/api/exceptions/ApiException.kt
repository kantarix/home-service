package com.kantarix.home_service.api.exceptions

import org.springframework.http.HttpStatus

class ApiException(
    val httpStatus: HttpStatus,
    val code: String,
    override val message: String,
) : RuntimeException()