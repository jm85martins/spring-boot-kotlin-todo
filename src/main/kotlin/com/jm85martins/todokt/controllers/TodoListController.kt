package com.jm85martins.todokt.controllers

import com.jm85martins.todokt.controllers.resources.TodoListResourceAssembler
import com.jm85martins.todokt.entities.TodoList
import com.jm85martins.todokt.exceptions.TodoListNotFoundException
import com.jm85martins.todokt.repositories.TodoListRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.Resource
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * Created by jorgemartins on 20/07/2017.
 */
@Controller
@RequestMapping("/{userId}/todo-list")
class TodoListController(val todoListRepository: TodoListRepository, val todoListResourceAssembler: TodoListResourceAssembler) {

    val logger = LoggerFactory.getLogger(TodoListController::class.java)

    @GetMapping
    fun getMyTodoLists(@PathVariable userId: String,
                       pageable: Pageable, assembler: PagedResourcesAssembler<TodoList>): ResponseEntity<PagedResources<Resource<*>>> {
        logger.info("Getting todo list for user {}", userId)

        val listPage = this.todoListRepository.findByUserId(userId, pageable)

        return ResponseEntity.ok(assembler.toResource(listPage, todoListResourceAssembler))
    }

    @GetMapping(path = arrayOf("/{listId}"))
    fun getTodoList(@PathVariable userId: String, @PathVariable listId: String): ResponseEntity<Resource<*>> {
        logger.info("Getting todo for user {} and todo list {}", userId, listId)

        val todoList = this.todoListRepository.findByIdAndUserId(listId, userId)

        if (!todoList.isPresent) {
            throw TodoListNotFoundException(listId, userId)
        }

        return ResponseEntity.ok(todoListResourceAssembler.toResource(todoList.get()))
    }

    @PostMapping
    fun newTodoList(@PathVariable userId: String, @RequestBody todoList: TodoList): ResponseEntity<Resource<*>> {
        var todoList = todoList
        logger.info("Creating todo for user {} with name {}", userId, todoList.name)

        todoList.userId = userId
        todoList = this.todoListRepository.insert(todoList)

        return ResponseEntity.ok(todoListResourceAssembler.toResource(todoList))
    }

    @PutMapping(path = arrayOf("/{listId}"))
    fun updateTodoList(@PathVariable userId: String, @PathVariable listId: String,
                       @RequestBody todoList: TodoList): ResponseEntity<Resource<*>> {
        logger.info("Updating todo for user {} with id {}", userId, listId)

        val managedList = this.todoListRepository.findByIdAndUserId(listId, userId)

        if (managedList.isPresent) {
            val toUpdate = managedList.get()
            toUpdate.name = todoList.name
            toUpdate.items = todoList.items
            this.todoListRepository.save(toUpdate)
            return ResponseEntity.ok(todoListResourceAssembler.toResource(toUpdate))
        } else {
            throw TodoListNotFoundException(listId, userId)
        }
    }
}