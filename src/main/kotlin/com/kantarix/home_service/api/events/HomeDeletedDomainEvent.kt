package com.kantarix.home_service.api.events

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("HOME_DELETED")
data class HomeDeletedDomainEvent(
    val homeId: Int,
) : DomainEvent(type = DomainEventType.HOME_DELETED)