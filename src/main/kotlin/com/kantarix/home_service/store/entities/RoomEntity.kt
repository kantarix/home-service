package com.kantarix.home_service.store.entities

import com.kantarix.home_service.api.dto.Room
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "rooms")
class RoomEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,

    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    val home: HomeEntity,

) {
    fun toRoomDto() =
        Room(
            id = id,
            name = name,
        )
}