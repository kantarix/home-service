package com.kantarix.home_service.api.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotBlank

data class RoomRequest (
    @field:NotBlank(message = "Name {javax.validation.constraints.NotBlank.message}")
    @field:Length(min = 2, message = "Name {org.hibernate.validator.constraints.Length.message}")
    @JsonProperty("name")
    private val _name: String?,
) {
    val name: String
        get() = _name!!
}