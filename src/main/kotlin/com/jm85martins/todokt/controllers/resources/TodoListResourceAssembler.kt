package com.jm85martins.todokt.controllers.resources

import com.jm85martins.todokt.controllers.TodoListController
import com.jm85martins.todokt.entities.TodoList
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.hateoas.mvc.ResourceAssemblerSupport
import org.springframework.stereotype.Component


/**
 * Created by jorgemartins on 19/07/2017.
 */
@Component
class TodoListResourceAssembler : ResourceAssemblerSupport<TodoList, Resource<*>>(TodoListController::class.java, Resource::class.java) {

    override fun toResource(todoList: TodoList): Resource<*> {
        return Resource<TodoList>(todoList, linkTo(methodOn(TodoListController::class.java, todoList.id)
                .getTodoList(todoList.userId!!, todoList.id!!)).withSelfRel())
    }

    override fun toResources(todoList: Iterable<TodoList>): List<Resource<*>> {
        val resources = todoList.map {
            Resource<TodoList>(it, linkTo(methodOn(TodoListController::class.java, it.id)
                    .getTodoList(it.userId!!, it.id!!)).withSelfRel())
        }
        return resources
    }
}
