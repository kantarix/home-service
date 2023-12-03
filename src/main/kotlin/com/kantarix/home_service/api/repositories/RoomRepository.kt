package com.kantarix.home_service.api.repositories

import com.kantarix.home_service.store.entities.RoomEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<RoomEntity, Int> {

    @EntityGraph(attributePaths = ["home"])
    fun findRoomEntityById(id: Int): RoomEntity?

}