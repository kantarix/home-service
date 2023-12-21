package com.kantarix.home_service.api.dto

data class Home(
    val id: Int,
    val name: String,
    val address: String?,
    val rooms: List<Room>,
)
