package com.kantarix.home_service.api.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import com.kantarix.home_service.api.dto.request.HomeRequest
import com.kantarix.home_service.api.events.DomainEvent
import com.kantarix.home_service.api.events.HomeDeletedDomainEvent
import com.kantarix.home_service.api.exceptions.ApiError
import com.kantarix.home_service.api.repositories.HomeRepository
import com.kantarix.home_service.api.repositories.OutboxMessageRepository
import com.kantarix.home_service.store.entities.HomeEntity
import com.kantarix.home_service.store.entities.OutboxMessageEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class HomeService(
    private val homeRepository: HomeRepository,
    private val outboxMessageRepository: OutboxMessageRepository,
    private val mapper: ObjectMapper,
) {

    @Transactional(readOnly = true)
    fun getHomes(ownerId: Int): List<HomeSimple> =
        homeRepository.findAllByOwnerId(ownerId)
            .map { it.toHomeSimpleDto() }

    @Transactional(readOnly = true)
    fun getHome(homeId: Int): Home =
        homeRepository.findHomeEntityById(homeId)
            ?.toHomeDto()
            ?: throw ApiError.HOME_NOT_FOUND.toException()

    @Transactional
    fun createHome(ownerId: Int, home: HomeRequest): Home =
        home.toEntity(ownerId)
            .let { homeRepository.save(it) }
            .toHomeDto()

    @Transactional
    fun editHome(homeId: Int, home: HomeRequest): Home =
        homeRepository.findByIdOrNull(homeId)
            ?.let { homeRepository.save(home.toEntity(homeId)) }
            ?.toHomeDto()
            ?: throw ApiError.HOME_NOT_FOUND.toException()

    @Transactional
    fun deleteHome(homeId: Int) {
        homeRepository.findByIdOrNull(homeId)
            ?.let {
                homeRepository.deleteById(it.id)
                saveHomeDeletedEvent(it.id)
            }
            ?: throw ApiError.HOME_NOT_FOUND.toException()
    }

    @Transactional
    fun deleteHomesByOwnerId(ownerId: Int) {
        homeRepository.findAllByOwnerId(ownerId)
            .map { it.id }
            .let {
                homeRepository.deleteAllById(it)
                it.map(::saveHomeDeletedEvent)
            }
    }

    @Transactional(readOnly = true)
    fun checkOwnership(homeId: Int, ownerId: Int): Boolean =
        homeRepository.findByIdOrNull(homeId)
            ?.checkIsAccessAllowed(ownerId)
            ?: throw ApiError.HOME_NOT_FOUND.toException()

    fun HomeEntity.checkIsAccessAllowed(ownerId: Int) =
        (this.ownerId == ownerId).takeIf { it }

    private fun HomeRequest.toEntity(ownerId: Int, id: Int = -1) =
        HomeEntity (
            id = id,
            ownerId = ownerId,
            name = name,
            address = address,
        )

    private fun DomainEvent.toJsonString() =
        mapper.writeValueAsString(this)

    private fun saveHomeDeletedEvent(homeId: Int) =
        outboxMessageRepository.save(
            OutboxMessageEntity(
                topic = "HOME_DELETED",
                message = HomeDeletedDomainEvent(homeId).toJsonString()
            )
        )
}