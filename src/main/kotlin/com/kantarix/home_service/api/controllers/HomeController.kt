package com.kantarix.home_service.api.controllers

import com.kantarix.home_service.api.dto.HomeDto
import com.kantarix.home_service.api.services.HomeService
import com.kantarix.home_service.store.entities.Home
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/homes")
class HomeController(
    private val homeService: HomeService
) {

    @GetMapping()
    fun getHomes(): List<Home> = homeService.getHomes()

    @GetMapping("/{homeId}")
    fun getHome(
        @PathVariable("homeId") homeId: Int,
    ): Home = homeService.getHome(homeId)

    @PostMapping
    fun createHome(
        @Validated @RequestBody homeDto: HomeDto,
    ): Home = homeService.createHome(homeDto)

    @PutMapping("/{homeId}")
    fun editHome(
        @PathVariable("homeId") homeId: Int,
        @Validated @RequestBody homeDto: HomeDto,
    ): Home = homeService.editHome(homeId, homeDto)

    @DeleteMapping("/{homeId}")
    fun deleteHome(
        @PathVariable("homeId") homeId: Int,
    ) = homeService.deleteHome(homeId)

}