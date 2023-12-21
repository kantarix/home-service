package com.kantarix.home_service.api.dto

data class ErrorDto(
    val code: String,
    val messages: List<String>,
)