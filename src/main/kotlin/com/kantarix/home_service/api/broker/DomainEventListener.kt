package com.kantarix.home_service.api.broker

import com.kantarix.home_service.api.events.UserDeletedDomainEvent
import com.kantarix.home_service.api.services.HomeService
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class DomainEventListener(
    private val homeService: HomeService,
) {

    private val log = KotlinLogging.logger {  }

    @KafkaListener(
        topics = ["USER_DELETED"],
        containerFactory = "kafkaDomainEventListenerContainerFactory"
    )
    fun listen(event: UserDeletedDomainEvent) {
        log.info { "Received event: $event" }
        homeService.deleteHomesByOwnerId(event.userId)
    }

}