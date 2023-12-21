package com.kantarix.home_service.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kantarix.home_service.andExpectJson
import com.kantarix.home_service.andExpectValidationError
import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import com.kantarix.home_service.api.dto.request.HomeRequest
import com.kantarix.home_service.api.exceptions.GlobalExceptionHandler
import com.kantarix.home_service.api.services.HomeService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@WebMvcTest
@Import(HomeController::class)
@ContextConfiguration(classes = [GlobalExceptionHandler::class])
class HomeControllerTest {

    @MockkBean
    private lateinit var homeService: HomeService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

    @Nested
    inner class GetHomes {

        @Test
        fun success() {
            val ownerId = 1
            val expectedHomes = emptyList<HomeSimple>()

            every { homeService.getHomes(ownerId) }.returns(expectedHomes)

            doRequest(ownerId)
                .andExpectJson(expectedHomes)
        }

        private fun doRequest(ownerId: Int): ResultActions {
            return mockMvc.perform(
                MockMvcRequestBuilders.get("/api/homes")
                    .param("ownerId", ownerId.toString())
            )
        }

    }

    @Nested
    inner class GetHome {

        @Test
        fun success() {
            val homeId = 1
            val expectedHome = createHome()

            every { homeService.getHome(homeId) }.returns(expectedHome)

            doRequest(homeId)
                .andExpectJson(expectedHome)
        }

        private fun doRequest(homeId: Int): ResultActions {
            return mockMvc.perform(
                MockMvcRequestBuilders.get("/api/homes/$homeId")
            )
        }

    }

    @Nested
    inner class CreateHome {

        @Test
        fun success() {
            val ownerId = 1
            val request = createHomeRequest()
            val expectedHome = createHome(request)

            every { homeService.createHome(ownerId, request) }.returns(expectedHome)

            doRequest(ownerId, request).andExpectJson(expectedHome)
        }

        @ParameterizedTest
        @CsvSource(delimiter = '|', textBlock = """
            ''      | ''
            home 1  | ''
            ''      | valid 14
            home 1  | invalid home address      """)
        fun `should throw an exception if validation failed`(
            name: String?,
            address: String?,
        ) {
            val ownerId = 1
            val request = HomeRequest(
                _name = name ?: "",
                address = address ?: "",
            )

            doRequest(ownerId, request)
                .andExpectValidationError()
        }

        private fun doRequest(ownerId: Int, request: HomeRequest): ResultActions {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/homes")
                    .param("ownerId", ownerId.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            )
        }

    }

    @Nested
    inner class EditHome {

        @Test
        fun success() {
            val homeId = 1
            val request = createHomeRequest()
            val expectedHome = createHome(request)

            every { homeService.editHome(homeId, request) }.returns(expectedHome)

            doRequest(homeId, request).andExpectJson(expectedHome)
        }

        @ParameterizedTest
        @CsvSource(delimiter = '|', textBlock = """
            ''      | ''
            home 1  | ''
            ''      | valid 14
            home 1  | invalid home address      """)
        fun `should throw an exception if validation failed`(
            name: String?,
            address: String?,
        ) {
            val homeId = 1
            val request = HomeRequest(
                _name = name ?: "",
                address = address ?: "",
            )

            doRequest(homeId, request)
                .andExpectValidationError()
        }

        private fun doRequest(homeId: Int, request: HomeRequest): ResultActions {
            return mockMvc.perform(
                MockMvcRequestBuilders.put("/api/homes/$homeId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            )
        }

    }

    private fun createHomeRequest(
        name: String? = null,
        address: String? = null,
    ) = HomeRequest(
        _name = name ?: "home 1",
        address = address ?: "Green 15",
    )

    private fun createHome(request: HomeRequest? = null) =
        Home(
            id = 1,
            name = request?.name ?: "home 1",
            address = request?.address ?: "Green 15",
            rooms = emptyList(),
        )

}