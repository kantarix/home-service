package com.kantarix.home_service.store.entities

import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "homes")
class HomeEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = -1,

    val name: String,

    val address: String?,

) {
    fun toHomeDto() =
        Home (
            id = id,
            name = name,
            address = address,
        )

    fun toHomeSimpleDto() =
        HomeSimple (
            id = id,
            name = name,
        )
}