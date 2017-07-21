package com.jm85martins.todokt.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Created by jorgemartins on 20/07/2017.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class TodoListNotFoundException(listId: String, userId: String) : BusinessException(TodoListNotFoundException.userMessageTemplate, String.format(TodoListNotFoundException.developerMessageTemplate, listId, userId)) {
    companion object {
        private val developerMessageTemplate = "Todo list with id %s for user %s not found."
        private val userMessageTemplate = "Todo List not found for the given id."
    }
}