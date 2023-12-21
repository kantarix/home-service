package com.kantarix.home_service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kantarix.home_service.api.dto.ErrorDto
import com.kantarix.home_service.api.exceptions.ApiError
import com.kantarix.home_service.api.exceptions.ApiException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.nio.charset.StandardCharsets

private val mapper = jacksonObjectMapper()

fun expectApiException(apiError: ApiError, executable: () -> Unit) {
    val ex = assertThrows<ApiException> { executable() }
    assertEquals(apiError.name, ex.code)
}

fun ResultActions.andExpectJson(expectedData: Any) {
    assertEquals(
        mapper.writeValueAsString(expectedData),
        andReturn().response.getContentAsString(StandardCharsets.UTF_8)
    )
}

fun ResultActions.andExpectValidationError() {
    val contentAsString = andReturn().response.getContentAsString(StandardCharsets.UTF_8)
    val ex = mapper.readValue(contentAsString, ErrorDto::class.java)
    assertEquals("VALIDATION_EXCEPTION", ex.code)
    this.andExpect(MockMvcResultMatchers.status().isBadRequest)
}