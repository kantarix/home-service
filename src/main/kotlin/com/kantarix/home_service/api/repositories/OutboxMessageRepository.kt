package com.kantarix.home_service.api.repositories

import com.kantarix.home_service.store.entities.OutboxMessageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OutboxMessageRepository : JpaRepository<OutboxMessageEntity, Int>