package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class HomeRequest(
    @field:NotBlank
    @JsonProperty("name")
    private val _name: String?,

    @field:NotBlank
    @field:Pattern(
        regexp = "[A-Z]{1}[a-z]+ \\d{1,4}",
        message = "{validation.home.address.invalid-format}"
    )
    val address: String?,
) {
    val name: String
        get() = _name!!
}