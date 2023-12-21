package com.kantarix.home_service.api.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.request.HomeRequest
import com.kantarix.home_service.api.events.DomainEventType
import com.kantarix.home_service.api.exceptions.ApiError
import com.kantarix.home_service.api.repositories.HomeRepository
import com.kantarix.home_service.api.repositories.OutboxMessageRepository
import com.kantarix.home_service.api.services.container.PostgresTestContainer
import com.kantarix.home_service.expectApiException
import com.kantarix.home_service.store.entities.HomeEntity
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.*
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

@Import(HomeService::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [],
    initializers = [PostgresTestContainer.Initializer::class]
)
@Sql(scripts=["classpath:cleanup.sql"], executionPhase=Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(SpringExtension::class)
class HomeServiceTest {

    @Autowired
    private lateinit var homeService: HomeService

    @Autowired
    private lateinit var homeRepository: HomeRepository

    @Autowired
    private lateinit var outboxMessageRepository: OutboxMessageRepository

    @MockkBean
    private lateinit var mapper: ObjectMapper

    @Nested
    inner class GetHomes {

        @Test
        fun success() {
            val existingHome = createHomeEntity()
            homeRepository.save(existingHome)
            assertTrue(homeRepository.existsById(existingHome.id))

            val ownerId = 1
            val expectedHomes = listOf(existingHome.toHomeSimpleDto())

            val homes = homeService.getHomes(ownerId)

            assertEquals(expectedHomes, homes)
        }

    }

    @Nested
    inner class GetHome {

        @Test
        fun success() {
            val existingHome = createHomeEntity()
            homeRepository.save(existingHome)
            assertTrue(homeRepository.existsById(existingHome.id))

            val homeId = existingHome.id
            val expectedHome = existingHome.toHomeDto()

            val home = homeService.getHome(homeId)

            assertEquals(expectedHome, home)
        }

        @Test
        fun `should throw an exception if home does not exist`() {
            val homeId = 1
            assertFalse(homeRepository.existsById(homeId))

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.getHome(homeId)
            }
        }

    }

    @Nested
    inner class CreateHome {

        @Test
        fun success() {
            val ownerId = 1
            val request = createHomeRequest()
            val expectedHome = createHome(request)

            val home = homeService.createHome(ownerId, request)

            assertEquals(expectedHome, home)
        }

    }

    @Nested
    inner class EditHome {

        @Test
        fun success() {
            val existingHome = createHomeEntity()
            homeRepository.save(existingHome)
            assertTrue(homeRepository.existsById(existingHome.id))

            val homeId = existingHome.id
            val request = createHomeRequest(name = "another", address = "another 200")
            val expectedHome = createHome(request)

            val home = homeService.editHome(homeId, request)

            assertEquals(expectedHome, home)
        }

        @Test
        fun `should throw an exception if home does not exist`() {
            val homeId = 1
            val request = createHomeRequest(name = "another", address = "another 200")
            assertFalse(homeRepository.existsById(homeId))

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.editHome(homeId, request)
            }
        }

    }

    @Nested
    inner class DeleteHome {

        @Test
        fun success() {
            val existingHome = createHomeEntity()
            homeRepository.save(existingHome)
            assertTrue(homeRepository.existsById(existingHome.id))

            every { mapper.writeValueAsString(any()) }.returns("")

            homeService.deleteHome(existingHome.id)
            assertFalse(homeRepository.existsById(existingHome.id))
            assertEquals(
                outboxMessageRepository.findByIdOrNull(1)?.topic,
                DomainEventType.HOME_DELETED.name
            )
        }

        @Test
        fun `should throw an exception if home does not exist`() {
            val homeId = 1
            assertFalse(homeRepository.existsById(homeId))

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.deleteHome(homeId)
            }
        }

    }

    @Nested
    inner class DeleteHomesByOwnerId {

        @Test
        fun success() {
            val existingHome = createHomeEntity()
            homeRepository.save(existingHome)
            assertTrue(homeRepository.existsById(existingHome.id))

            val ownerId = existingHome.ownerId
            every { mapper.writeValueAsString(any()) }.returns("")

            homeService.deleteHomesByOwnerId(ownerId)
            assertFalse(homeRepository.existsById(existingHome.id))
            assertEquals(
                outboxMessageRepository.findByIdOrNull(1)?.topic,
                DomainEventType.HOME_DELETED.name
            )
        }

    }

    @Nested
    inner class CheckOwnership {

        @Test
        fun success() {
            val existingHome = createHomeEntity()
            homeRepository.save(existingHome)
            assertTrue(homeRepository.existsById(existingHome.id))

            val homeId = existingHome.id
            val ownerId = existingHome.ownerId

            assertTrue(homeService.checkOwnership(homeId, ownerId))
        }

        @Test
        fun `should throw an exception if home does not exist or owner does not own home`() {
            val homeId = 1
            val ownerId = 1
            assertFalse(homeRepository.existsById(homeId))

            expectApiException(ApiError.HOME_NOT_FOUND) {
                homeService.checkOwnership(homeId, ownerId)
            }
        }

    }

    private fun createHomeRequest(
        name: String? = null,
        address: String? = null,
    ) = HomeRequest(
        _name = name ?: "home 1",
        address = address ?: "Green 15",
    )

    private fun createHome(request: HomeRequest) =
        Home(
            id = 1,
            name = request.name,
            address = request.address,
            rooms = emptyList(),
        )

    private fun createHomeEntity() =
        HomeEntity(
            id = 1,
            ownerId = 1,
            name = "home 1",
            address = "address 10",
            rooms = emptyList(),
        )

}