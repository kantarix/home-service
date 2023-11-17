package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class RoomRequest (
    @field:NotBlank
    @JsonProperty("name")
    private val _name: String?,
) {
    val name: String
        get() = _name!!
}