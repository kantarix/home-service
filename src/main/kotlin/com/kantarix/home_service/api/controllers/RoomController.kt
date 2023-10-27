package com.kantarix.home_service.api.controllers

import com.kantarix.home_service.api.dto.Room
import com.kantarix.home_service.api.dto.request.RoomRequest
import com.kantarix.home_service.api.services.RoomService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rooms")
class RoomController(
    private val roomService: RoomService
) {

    @PostMapping
    fun createRoom(
        @RequestParam homeId: Int,
        @Validated @RequestBody room: RoomRequest,
    ): Room = roomService.createRoom(homeId, room)

    @PutMapping("/{roomId}")
    fun editRoom(
        @PathVariable("roomId") roomId: Int,
        @Validated @RequestBody room: RoomRequest,
    ): Room = roomService.editRoom(roomId, room)

    @DeleteMapping("/{roomId}")
    fun deleteRoom(
        @PathVariable("roomId") roomId: Int
    ) = roomService.deleteRoom(roomId)

}