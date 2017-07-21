package com.jm85martins.todokt.repositories

import com.jm85martins.todokt.entities.TodoList
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Created by jorgemartins on 20/07/2017.
 */
@Repository
interface TodoListRepository : MongoRepository<TodoList, String> {
    fun findByUserId(userId: String, pageable: Pageable): Page<TodoList>

    fun findByIdAndUserId(id: String, userId: String): Optional<TodoList>
}