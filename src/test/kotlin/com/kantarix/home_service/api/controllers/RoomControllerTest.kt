package com.kantarix.home_service.api.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kantarix.home_service.andExpectJson
import com.kantarix.home_service.andExpectValidationError
import com.kantarix.home_service.api.dto.Room
import com.kantarix.home_service.api.dto.request.RoomRequest
import com.kantarix.home_service.api.exceptions.GlobalExceptionHandler
import com.kantarix.home_service.api.services.RoomService
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
@Import(RoomController::class)
@ContextConfiguration(classes = [GlobalExceptionHandler::class])
class RoomControllerTest {

    @MockkBean
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper()

    @Nested
    inner class CreateRoom {

        @Test
        fun success() {
            val homeId = 1
            val request = createRoomRequest()
            val expectedRoom = createRoom(request)

            every { roomService.createRoom(homeId, request) }.returns(expectedRoom)

            doRequest(homeId, request).andExpectJson(expectedRoom)
        }

        @ParameterizedTest
        @CsvSource(delimiter = '|', textBlock = """
            ''                                  """)
        fun `should throw an exception if validation failed`(
            name: String?,
        ) {
            val homeId = 1
            val request = RoomRequest(
                _name = name ?: "",
            )

            doRequest(homeId, request).andExpectValidationError()
        }

        private fun doRequest(homeId: Int, request: RoomRequest): ResultActions {
            return mockMvc.perform(
                MockMvcRequestBuilders.post("/api/rooms")
                    .param("homeId", homeId.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            )
        }

    }

    @Nested
    inner class EditRoom {

        @Test
        fun success() {
            val roomId = 1
            val request = createRoomRequest()
            val expectedRoom = createRoom(request)

            every { roomService.editRoom(roomId, request) }.returns(expectedRoom)

            doRequest(roomId, request).andExpectJson(expectedRoom)
        }

        @ParameterizedTest
        @CsvSource(delimiter = '|', textBlock = """
            ''                                  """)
        fun `should throw an exception if validation failed`(
            name: String?,
        ) {
            val roomId = 1
            val request = RoomRequest(
                _name = name ?: "",
            )

            doRequest(roomId, request).andExpectValidationError()
        }

        private fun doRequest(roomId: Int, request: RoomRequest): ResultActions {
            return mockMvc.perform(
                MockMvcRequestBuilders.put("/api/rooms/$roomId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsBytes(request))
            )
        }

    }

    private fun createRoomRequest(
        name: String? = null,
    ) = RoomRequest(
        _name = name ?: "home 1",
    )

    private fun createRoom(request: RoomRequest) =
        Room(
            id = 1,
            name = request.name,
        )

}