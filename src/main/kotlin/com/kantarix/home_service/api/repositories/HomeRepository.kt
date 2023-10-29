package com.kantarix.home_service.api.repositories

import com.kantarix.home_service.store.entities.HomeEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface HomeRepository : JpaRepository<HomeEntity, Int> {

    @EntityGraph(attributePaths = ["rooms"])
    override fun findById(id: Int): Optional<HomeEntity>

}