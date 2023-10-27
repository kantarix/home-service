package com.kantarix.home_service.api.services

import com.kantarix.home_service.api.dto.Room
import com.kantarix.home_service.api.dto.request.RoomRequest
import com.kantarix.home_service.api.exceptions.ApiError
import com.kantarix.home_service.api.repositories.HomeRepository
import com.kantarix.home_service.api.repositories.RoomRepository
import com.kantarix.home_service.store.entities.HomeEntity
import com.kantarix.home_service.store.entities.RoomEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class RoomService(
    private val homeRepository: HomeRepository,
    private val roomRepository: RoomRepository,
) {

    @Transactional
    fun createRoom(homeId: Int, room: RoomRequest): Room =
        homeRepository.findByIdOrNull(homeId)
            ?.let { room.toEntity(home = it) }
            ?.let { roomRepository.save(it) }
            ?.toRoomDto()
            ?: throw ApiError.HOME_NOT_FOUND.toException()

    @Transactional
    fun editRoom(roomId: Int, room: RoomRequest): Room =
        roomRepository.findByIdOrNull(roomId)
            ?.let { roomRepository.save(room.toEntity(id = roomId, home = it.home)) }
            ?.toRoomDto()
            ?: throw ApiError.ROOM_NOT_FOUND.toException()

    @Transactional
    fun deleteRoom(roomId: Int) =
        roomRepository.findByIdOrNull(roomId)
            ?.let { roomRepository.deleteById(roomId) }
            ?: throw ApiError.ROOM_NOT_FOUND.toException()

    private fun RoomRequest.toEntity(id: Int = -1, home: HomeEntity) =
        RoomEntity(
            id = id,
            name = name,
            home = home,
        )

}