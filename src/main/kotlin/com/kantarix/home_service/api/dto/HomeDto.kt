package com.kantarix.home_service.api.dto

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Pattern


data class HomeDto(
    val id: Int,

    @field:Length(min = 2, message = "Name length must be greater than 2.")
    val name: String,

    @field:Length(min = 5, message = "Address length must be greater than 5.")
    @field:Pattern(
        regexp = "[A-Z]{1}[a-z]+ \\d{1,4}",
        message = "Address must consist of the street name and house number separated by a space."
    )
    val address: String?,
)
