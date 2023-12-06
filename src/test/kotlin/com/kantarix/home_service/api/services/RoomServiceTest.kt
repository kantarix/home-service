package com.kantarix.home_service.api.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.kantarix.home_service.api.dto.Room
import com.kantarix.home_service.api.dto.request.RoomRequest
import com.kantarix.home_service.api.events.DomainEventType
import com.kantarix.home_service.api.exceptions.ApiError
import com.kantarix.home_service.api.repositories.HomeRepository
import com.kantarix.home_service.api.repositories.OutboxMessageRepository
import com.kantarix.home_service.api.repositories.RoomRepository
import com.kantarix.home_service.api.services.container.PostgresTestContainer
import com.kantarix.home_service.expectApiException
import com.kantarix.home_service.store.entities.HomeEntity
import com.kantarix.home_service.store.entities.RoomEntity
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension

@Import(RoomService::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [],
    initializers = [PostgresTestContainer.Initializer::class]
)
@Sql(scripts=["classpath:cleanup.sql"], executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension::class)
class RoomServiceTest {

    @Autowired
    private lateinit var roomService: RoomService

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Autowired
    private lateinit var homeRepository: HomeRepository

    @Autowired
    private lateinit var outboxMessageRepository: OutboxMessageRepository

    @MockkBean
    private lateinit var mapper: ObjectMapper

    @Nested
    inner class CreateRoom {

        @Test
        fun success() {
            val homeId = 1
            val request = createRoomRequest()
            val expectedRoom = createRoom(request)
            homeRepository.save(createRoomEntity().home)

            val room = roomService.createRoom(homeId, request)

            Assertions.assertEquals(expectedRoom, room)
        }

        @Test
        fun `should throw an exception if home does not exist`() {
            val homeId = 1
            val request = createRoomRequest()

            expectApiException(ApiError.HOME_NOT_FOUND) {
                roomService.createRoom(homeId, request)
            }
        }

    }

    @Nested
    inner class EditRoom {

        @Test
        fun success() {
            val existingRoom = createRoomEntity()
            homeRepository.save(existingRoom.home)
            roomRepository.save(existingRoom)
            Assertions.assertTrue(roomRepository.existsById(existingRoom.id))

            val roomId = existingRoom.id
            val request = createRoomRequest(name = "another")
            val expectedRoom = createRoom(request)

            val room = roomService.editRoom(roomId, request)

            Assertions.assertEquals(expectedRoom, room)
        }

        @Test
        fun `should throw an exception if room does not exist`() {
            val roomId = 1
            val request = createRoomRequest(name = "another")
            Assertions.assertFalse(roomRepository.existsById(roomId))

            expectApiException(ApiError.ROOM_NOT_FOUND) {
                roomService.editRoom(roomId, request)
            }
        }

    }

    @Nested
    inner class DeleteRoom {

        @Test
        fun success() {
            val existingRoom = createRoomEntity()
            homeRepository.save(existingRoom.home)
            roomRepository.save(existingRoom)
            Assertions.assertTrue(roomRepository.existsById(existingRoom.id))

            every { mapper.writeValueAsString(any()) }.returns("")

            roomService.deleteRoom(existingRoom.id)
            Assertions.assertFalse(roomRepository.existsById(existingRoom.id))
            Assertions.assertEquals(
                outboxMessageRepository.findByIdOrNull(1)?.topic,
                DomainEventType.ROOM_DELETED.name
            )
        }

        @Test
        fun `should throw an exception if room does not exist`() {
            val roomId = 1
            Assertions.assertFalse(roomRepository.existsById(roomId))

            expectApiException(ApiError.ROOM_NOT_FOUND) {
                roomService.deleteRoom(roomId)
            }
        }

    }

    @Nested
    inner class CheckOwnership {

        @Test
        fun success() {
            val existingRoom = createRoomEntity()
            homeRepository.save(existingRoom.home)
            roomRepository.save(existingRoom)
            Assertions.assertTrue(roomRepository.existsById(existingRoom.id))

            val roomId = existingRoom.id
            val ownerId = existingRoom.home.ownerId

            Assertions.assertTrue(roomService.checkOwnership(roomId, ownerId))
        }

        @Test
        fun `should throw an exception if room does not exist or owner does not own home with room`() {
            val roomId = 1
            val ownerId = 1
            Assertions.assertFalse(roomRepository.existsById(roomId))

            expectApiException(ApiError.ROOM_NOT_FOUND) {
                roomService.checkOwnership(roomId, ownerId)
            }
        }

    }

    private fun createRoomRequest(
        name: String? = null,
    ) = RoomRequest(
        _name = name ?: "room 1",
    )

    private fun createRoom(request: RoomRequest) =
        Room(
            id = 1,
            name = request.name,
        )

    private fun createRoomEntity() =
        RoomEntity(
            id = 1,
            name = "room 1",
            home = HomeEntity(
                id = 1,
                ownerId = 1,
                name = "home 1",
                address = "address 10",
            )
        )

}