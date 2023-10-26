package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class HomeRequest(
    @field:NotNull(message = "Name {validation.field.null}")
    @field:NotBlank(message = "Name {validation.field.blank}")
    @field:Length(min = 2, message = "Name {validation.field.min-length}")
    @JsonProperty("name")
    private val _name: String?,

    @field:Length(min = 5, message = "Address {validation.field.min-length}")
    @field:Pattern(
        regexp = "[A-Z]{1}[a-z]+ \\d{1,4}",
        message = "{validation.home.address.invalid-format}"
    )
    val address: String?,
) {
    val name: String
        get() = _name!!
}