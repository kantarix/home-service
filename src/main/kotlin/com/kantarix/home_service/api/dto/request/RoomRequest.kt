package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RoomRequest (
    @field:NotNull(message = "Name {validation.field.null}")
    @field:NotBlank(message = "Name {validation.field.blank}")
    @field:Length(min = 2, message = "Name {validation.field.min-length}")
    @JsonProperty("name")
    private val _name: String?,
) {
    val name: String
        get() = _name!!
}