package com.kantarix.home_service.api.repositories

import com.kantarix.home_service.store.entities.Home
import org.springframework.data.jpa.repository.JpaRepository

interface HomeRepository : JpaRepository<Home, Int> {

}