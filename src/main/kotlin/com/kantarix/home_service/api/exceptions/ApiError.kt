package com.kantarix.home_service.api.exceptions

import org.springframework.http.HttpStatus

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String,
) {
    HOME_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "apiErrors.HOME_NOT_FOUND"
    ),
    ROOM_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "apiErrors.ROOM_NOT_FOUND"
    ),
    ;

    fun toException() =
        ApiException(
            httpStatus = httpStatus,
            code = name,
            message = message,
        )

}