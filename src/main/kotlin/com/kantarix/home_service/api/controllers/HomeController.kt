package com.kantarix.home_service.api.controllers

import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import com.kantarix.home_service.api.dto.request.HomeRequest
import com.kantarix.home_service.api.services.HomeService
import com.kantarix.home_service.store.entities.HomeEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/homes")
class HomeController(
    private val homeService: HomeService
) {

    @GetMapping()
    fun getHomes(): List<HomeSimple> = homeService.getHomes()

    @GetMapping("/{homeId}")
    fun getHome(
        @PathVariable("homeId") homeId: Int,
    ): Home = homeService.getHome(homeId)

    @PostMapping
    fun createHome(
        @Validated @RequestBody home: HomeRequest,
    ): Home = homeService.createHome(home)

    @PutMapping("/{homeId}")
    fun editHome(
        @PathVariable("homeId") homeId: Int,
        @Validated @RequestBody home: HomeRequest,
    ): Home = homeService.editHome(homeId, home)

    @DeleteMapping("/{homeId}")
    fun deleteHome(
        @PathVariable("homeId") homeId: Int,
    ) = homeService.deleteHome(homeId)

}