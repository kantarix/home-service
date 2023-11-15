package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class HomeRequest(
    @field:NotBlank(message = "Name {javax.validation.constraints.NotBlank.message}")
    @field:Length(min = 2, message = "Name {org.hibernate.validator.constraints.Length.message}")
    @JsonProperty("name")
    private val _name: String?,

    @field:Length(min = 5, message = "Address {org.hibernate.validator.constraints.Length.message}")
    @field:Pattern(
        regexp = "[A-Z]{1}[a-z]+ \\d{1,4}",
        message = "{validation.home.address.invalid-format}"
    )
    val address: String?,
) {
    val name: String
        get() = _name!!
}