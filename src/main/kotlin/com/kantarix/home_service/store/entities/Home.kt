package com.kantarix.home_service.store.entities

import javax.persistence.*


@Entity
@Table(name = "home")
data class Home(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    val name: String,

    val address: String?,

)