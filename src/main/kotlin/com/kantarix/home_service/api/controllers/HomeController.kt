package com.kantarix.home_service.api.controllers

import com.kantarix.home_service.api.dto.Home
import com.kantarix.home_service.api.dto.HomeSimple
import com.kantarix.home_service.api.dto.request.HomeRequest
import com.kantarix.home_service.api.services.HomeService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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