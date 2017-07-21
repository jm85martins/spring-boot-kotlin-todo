package com.jm85martins.todokt.entities

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceConstructor

/**
 * Created by jorgemartins on 20/07/2017.
 */
data class TodoList @PersistenceConstructor constructor(
        @Id val id: String? = null,
        var name: String,
        var userId: String?,
        var items: List<Item>? = emptyList()
)
