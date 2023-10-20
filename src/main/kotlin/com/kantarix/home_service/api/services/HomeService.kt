package com.kantarix.home_service.api.services

import com.kantarix.home_service.api.dto.HomeDto
import com.kantarix.home_service.api.repositories.HomeRepository
import com.kantarix.home_service.store.entities.Home
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class HomeService(
    private val homeRepository: HomeRepository
) {

    @Transactional(readOnly = true)
    fun getHomes(): List<Home> = homeRepository.findAll()

    @Transactional(readOnly = true)
    fun getHome(homeId: Int): Home = homeRepository.findById(homeId).get()

    fun createHome(homeDto: HomeDto): Home =
        homeRepository.save(
            Home(
                name = homeDto.name,
                address = homeDto.address,
            )
        )

    fun editHome(homeId: Int, homeDto: HomeDto): Home {
        val home = homeRepository.findById(homeId).get()

        return homeRepository.save(
            home.copy(
                name = homeDto.name,
                address = homeDto.address ?: home.address,
            )
        )
    }

    fun deleteHome(homeId: Int) {
        homeRepository.findById(homeId).get()
        homeRepository.deleteById(homeId)
    }

}