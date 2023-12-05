package com.kantarix.home_service.api.events

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("ROOM_DELETED")
data class RoomDeletedDomainEvent(
    val roomId: Int,
) : DomainEvent(type = DomainEventType.ROOM_DELETED)