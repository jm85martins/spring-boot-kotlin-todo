package com.jm85martins.todokt

import com.jm85martins.todokt.entities.TodoList
import com.jm85martins.todokt.repositories.TodoListRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class TodoktApplication

fun main(args: Array<String>) {
    SpringApplication.run(TodoktApplication::class.java, *args)
}

@Bean
internal fun init(todoListRepository: TodoListRepository) = CommandLineRunner {
    "todo list 1,todo list 2,todo list 3,todo list 4,todo list 5"
            .split(",")
            .forEach{v -> todoListRepository.save(TodoList(name = v, userId = "123"))}
}

