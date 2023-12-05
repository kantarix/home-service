package com.kantarix.home_service.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kantarix.home_service.api.events.DomainEvent
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.stereotype.Component

@Component
class DomainEventDeserializer : Deserializer<DomainEvent> {

    private val mapper = jacksonObjectMapper()

    override fun deserialize(topic: String, data: ByteArray): DomainEvent =
        mapper.readValue(data, DomainEvent::class.java)

}