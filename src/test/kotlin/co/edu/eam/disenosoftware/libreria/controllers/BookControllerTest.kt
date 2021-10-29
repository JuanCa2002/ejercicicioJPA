package co.edu.eam.disenosoftware.libreria.controllers

import co.edu.eam.disenosoftware.libreria.exceptions.ErrorResponse
import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.Borrow
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import co.edu.eam.disenosoftware.libreria.models.entities.User
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

@SpringBootTest
@Transactional

@AutoConfigureMockMvc

class BookControllerTest {

    @Autowired
    lateinit var mocMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createBookHappyPath(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)

        val body = """
           {
            "codigo": "123",
            "nombre": "Harry Potter",
            "isbn": "555",
            "stock": 24
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun createBookPublisherDoesNotExist(){
        val body = """
           {
            "codigo": "123",
            "nombre": "Harry Potter",
            "isbn": "555",
            "stock": 24
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"this publisher does not exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun createBookAlreadyExist(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        entityManager.persist(Book("111","Alicia","555",14,publisher))
        val body = """
           {
            "codigo": "111",
            "nombre": "Harry Potter",
            "isbn": "555",
            "stock": 24
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This book already exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun createBookAlreadyExistWithThisName(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        entityManager.persist(Book("111","Alicia","555",14,publisher))
        val body = """
           {
            "codigo": "123",
            "nombre": "Alicia",
            "isbn": "555",
            "stock": 24
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/books/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This book with this name already exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun editBookHappyPath(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        entityManager.persist(Book("111","Alicia","555",14,publisher))
        val body = """
           {
            "nombre": "Harry Potter",
            "isbn": "555",
            "stock": 24
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .put("/books/111")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun editBookNotFoundBook(){
        val body = """
           {
            "nombre": "Harry Potter",
            "isbn": "555",
            "stock": 24
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .put("/books/111")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This book does not exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun findUsersByBookHappyPath(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val book= Book("111","Alicia","555",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","123")
        val user2= User("Juliana","Palaez","122")
        entityManager.persist(user)
        entityManager.persist(user2)
        entityManager.persist(Borrow(14, Date(2020,2,4),book,user))
        entityManager.persist(Borrow(15, Date(2020,2,4),book,user2))

        val request = MockMvcRequestBuilders.get("/books/111/users")
            .contentType(MediaType.APPLICATION_JSON)

        val response = mocMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isOk)

        val json = response.andReturn().response.contentAsString;

        val  users = objectMapper.readValue(json, Array<User>::class.java)
        Assertions.assertEquals(2,users.size)
        Assertions.assertEquals("Juan",users[0].name)
        Assertions.assertEquals("Torres",users[0].lastName)
        Assertions.assertEquals("123",users[0].identification)
        Assertions.assertEquals("Juliana",users[1].name)
        Assertions.assertEquals("Palaez",users[1].lastName)
        Assertions.assertEquals("122",users[1].identification)


    }

    @Test
    fun findUsersByBookNotFoundBook(){
        val request = MockMvcRequestBuilders.get("/books/111/users")
            .contentType(MediaType.APPLICATION_JSON)

        val response = mocMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isPreconditionFailed)

        val json = response.andReturn().response.contentAsString;

        val  error= objectMapper.readValue(json, ErrorResponse::class.java)
        Assertions.assertEquals("This book does not exist in the system", error.message)
        Assertions.assertEquals(412, error.code)
    }

}