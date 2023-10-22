package com.kantarix.home_service.api.services

import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import com.kantarix.home_service.api.dto.request.HomeRequest
import com.kantarix.home_service.api.repositories.HomeRepository
import com.kantarix.home_service.store.entities.HomeEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class HomeService(
    private val homeRepository: HomeRepository
) {

    @Transactional(readOnly = true)
    fun getHomes(): List<HomeSimple> = homeRepository.findAll().map { it.toHomeSimpleDto() }

    @Transactional(readOnly = true)
    fun getHome(homeId: Int): Home = homeRepository.findByIdOrNull(homeId)
        ?.toHomeDto()
        ?: throw NoSuchElementException("Home with id $homeId does not exist.")

    fun createHome(home: HomeRequest): Home =
        home.toEntity()
            .let { homeRepository.save(it) }
            .toHomeDto()

    fun editHome(homeId: Int, home: HomeRequest): Home =
        homeRepository.findByIdOrNull(homeId)
            ?.let { homeRepository.save(home.toEntity(homeId)) }
            ?.toHomeDto()
            ?: throw NoSuchElementException("Home with id $homeId does not exist.")

    fun deleteHome(homeId: Int) {
        homeRepository.findByIdOrNull(homeId)
            ?.let { homeRepository.deleteById(homeId) }
            ?: throw NoSuchElementException("Home with id $homeId does not exist.")
    }

    private fun HomeRequest.toEntity(id: Int = -1) =
        HomeEntity (
            id = id,
            name = name,
            address = address,
        )
}