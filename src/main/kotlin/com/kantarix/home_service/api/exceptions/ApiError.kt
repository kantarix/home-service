package com.kantarix.home_service.api.exceptions

import org.springframework.http.HttpStatus

enum class ApiError(
    val httpStatus: HttpStatus,
    val message: String,
) {
    HOME_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Home does not exist."
    ),
    ROOM_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "Room does not exist."
    ),
    ;

    fun toException() =
        ApiException(
            httpStatus = httpStatus,
            code = name,
            message = message,
        )

}