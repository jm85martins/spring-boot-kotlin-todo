package com.jm85martins.todokt.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.jm85martins.todokt.TodoktApplication
import com.jm85martins.todokt.entities.Item
import com.jm85martins.todokt.entities.TodoList
import com.jm85martins.todokt.repositories.TodoListRepository
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup
import org.springframework.web.context.WebApplicationContext
import java.nio.charset.Charset
import java.util.*


/**
 * Created by jorgemartins on 20/07/2017.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(TodoktApplication::class))
@WebAppConfiguration
class TodoListControllerTest {

    private val contentType = MediaType("application", "hal+json", Charset.forName("UTF-8"))

    private var mockMvc: MockMvc? = null

    private var mappingJackson2HttpMessageConverter: HttpMessageConverter<*>? = null

    @Autowired
    private val todoListRepository: TodoListRepository? = null

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    @Autowired
    var objectMapper: ObjectMapper? = null

    private val dummyUserId = "123"
    private val todoList = ArrayList<TodoList>()

    @Autowired
    internal fun setConverters(converters: Array<HttpMessageConverter<*>>) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(*converters).stream()
                .filter { hmc -> hmc is MappingJackson2HttpMessageConverter }
                .findAny()
                .orElse(null)

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter)
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext!!).build()

        this.todoListRepository!!.deleteAll()

        this.todoList.add(this.todoListRepository!!.save(TodoList(name = "My first TodoList", userId = dummyUserId)))
        this.todoList.add(this.todoListRepository!!.save(TodoList(name = "My second TodoList", userId = dummyUserId)))
    }

    @Test
    @Throws(Exception::class)
    fun todoListNotFound() {
        mockMvc!!.perform(get("/12345678/todo-list/123456")
                .contentType(contentType))
                .andExpect(status().isNotFound())
    }

    @Test
    @Throws(Exception::class)
    fun getUserTodoList() {
        mockMvc!!.perform(get("/$dummyUserId/todo-list?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath<Collection<*>>("$._embedded.todoListList", hasSize<Any>(2)))
                .andExpect(jsonPath("$._embedded.todoListList[0].id", `is`(this.todoList.get(0).id)))
                .andExpect(jsonPath("$._embedded.todoListList[0].name", `is`(this.todoList.get(0).name)))
                .andExpect(jsonPath("$._embedded.todoListList[1].id", `is`(this.todoList.get(1).id)))
                .andExpect(jsonPath("$._embedded.todoListList[1].name", `is`(this.todoList.get(1).name)))
    }

    @Test
    @Throws(Exception::class)
    fun getSingleTodoList() {
        mockMvc!!.perform(get("/" + dummyUserId + "/todo-list/"
                + this.todoList.get(0).id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", `is`(this.todoList.get(0).id)))
    }

    @Test
    @Throws(Exception::class)
    fun createNewTodoList() {

        val items = ArrayList<Item>()
        items.add(Item(name="Travel to Oporto"))
        items.add(Item(name="Eat Francesinha"))

        val todoList = TodoList(name = "My awesome todo list for Oporto Travel", userId = "123", items = items)

        mockMvc!!.perform(post("/12345678/todo-list")
                .content(objectMapper!!.writeValueAsString(todoList))
                .contentType(contentType))
                .andExpect(status().isOk())
                //.andExpect(redirectedUrlPattern("http://*/12345678/todo-list/*"));
                .andExpect(jsonPath("$.name", `is`(todoList.name)))
    }

}