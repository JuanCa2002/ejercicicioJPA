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
class UserControllerTest {
    @Autowired
    lateinit var mocMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createUserHappyPath(){
        val body = """
           {
            "name": "Juan",
            "lastName": "Torres",
            "identification": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun createUserAlreadyExist(){
        entityManager.persist(User("Juan","Torres","123"))
        val body = """
           {
            "name": "Juan",
            "lastName": "Torres",
            "identification": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This user already exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun findBorrowsByUserHappyPath(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val book= Book("111","Alicia","555",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","123")
        entityManager.persist(user)
        entityManager.persist(Borrow(14, Date(2020,2,4),book,user))
        entityManager.persist(Borrow(15, Date(2020,2,4),book,user))
        val request = MockMvcRequestBuilders.get("/users/123/borrows")
            .contentType(MediaType.APPLICATION_JSON)

        val response = mocMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isOk)

        val json = response.andReturn().response.contentAsString;

        val  borrows = objectMapper.readValue(json, Array<Borrow>::class.java)
        Assertions.assertEquals(2,borrows.size)
        borrows.forEach { Assertions.assertEquals("123",it.user?.identification) }
    }

    @Test
    fun findBorrowsByUserNotFoundUser(){
        val request = MockMvcRequestBuilders.get("/users/123/borrows")
            .contentType(MediaType.APPLICATION_JSON)

        val response = mocMvc.perform(request)
        response.andExpect(MockMvcResultMatchers.status().isPreconditionFailed)

        val json = response.andReturn().response.contentAsString;

        val  error= objectMapper.readValue(json, ErrorResponse::class.java)
        Assertions.assertEquals("This user does not exist in the system", error.message)
        Assertions.assertEquals(412, error.code)
    }
}