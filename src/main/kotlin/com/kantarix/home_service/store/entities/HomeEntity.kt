package com.kantarix.home_service.store.entities

import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "homes")
class HomeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,

    val ownerId: Int,

    val name: String,

    val address: String?,

    @OneToMany(mappedBy = "home", targetEntity = RoomEntity::class, fetch = FetchType.LAZY)
    var rooms: List<RoomEntity>? = null,

) {
    fun toHomeDto() =
        Home (
            id = id,
            name = name,
            address = address,
            rooms = rooms?.map { it.toRoomDto() } ?: emptyList(),
        )

    fun toHomeSimpleDto() =
        HomeSimple (
            id = id,
            name = name,
        )
}