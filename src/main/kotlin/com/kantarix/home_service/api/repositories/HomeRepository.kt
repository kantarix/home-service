package com.kantarix.home_service.api.repositories

import com.kantarix.home_service.store.entities.HomeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface HomeRepository : JpaRepository<HomeEntity, Int>