package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class HomeRequest(
    @field:NotNull
    @field:Length(min = 2, message = "Name length must be greater than 2.")
    @JsonProperty("name")
    private val _name: String?,

    @field:Length(min = 5, message = "Address length must be greater than 5.")
    @field:Pattern(
        regexp = "[A-Z]{1}[a-z]+ \\d{1,4}",
        message = "Address must consist of the street name and house number separated by a space."
    )
    val address: String?,
) {
    val name: String
        get() = _name!!
}