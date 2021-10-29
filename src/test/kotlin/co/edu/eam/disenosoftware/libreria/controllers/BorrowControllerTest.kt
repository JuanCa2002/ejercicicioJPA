package co.edu.eam.disenosoftware.libreria.controllers

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
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.Flow
import javax.persistence.EntityManager

@SpringBootTest
@Transactional

@AutoConfigureMockMvc
class BorrowControllerTest {

    @Autowired
    lateinit var mocMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createBorrowHappyPath(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        entityManager.persist(Book("111","Alicia","555",14,publisher))
        entityManager.persist(User("Juan","Torres","123"))
        val body = """
           {
            "id": 14,
            "bookId": "111",
            "userId": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/borrows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun createBorrowBookNotFound(){

        entityManager.persist(User("Juan","Torres","123"))
        val body = """
           {
            "id": 14,
            "bookId": "111",
            "userId": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/borrows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This book does not exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }
    @Test
    fun createBorrowUserNotFound(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        entityManager.persist(Book("111","Alicia","555",14,publisher))
        val body = """
           {
            "id": 14,
            "bookId": "111",
            "userId": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/borrows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This user does not exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }
    @Test
    fun createBorrowBookOnlyOneCopy(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        entityManager.persist(Book("111","Alicia","555",1,publisher))
        entityManager.persist(User("Juan","Torres","123"))
        val body = """
           {
            "id": 14,
            "bookId": "111",
            "userId": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/borrows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"Only one copy of the book remains, therefore it cannot be loaned\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun createBorrowBookUserAlreadyHasFiveBorrows(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val book= Book("111","Alicia","555",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","123")
        entityManager.persist(user)
        entityManager.persist(Borrow(14, Date(2020,2,4),book,user))
        entityManager.persist(Borrow(15, Date(2020,2,4),book,user))
        entityManager.persist(Borrow(16, Date(2020,2,4),book,user))
        entityManager.persist(Borrow(17, Date(2020,2,4),book,user))
        entityManager.persist(Borrow(18, Date(2020,2,4),book,user))
        val body = """
           {
            "id": 14,
            "bookId": "111",
            "userId": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/borrows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"The user already has 5 loans, therefore this cannot be done\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun createBorrowBookUserAlreadyHasBorrowWithThisBook(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val book= Book("111","Alicia","555",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","123")
        entityManager.persist(user)
        entityManager.persist(Borrow(14, Date(2020,2,4),book,user))
        val body = """
           {
            "id": 14,
            "bookId": "111",
            "userId": "123"
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/borrows")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"the user already made the loan of this book once, therefore the loan cannot be made\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }

    @Test
    fun deleteBorrowHappyTest(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val book= Book("111","Alicia","555",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","123")
        entityManager.persist(user)
        entityManager.persist(Borrow(14, Date(2020,2,4),book,user))
        val request = MockMvcRequestBuilders
            .delete("/borrows/14")
            .contentType(MediaType.APPLICATION_JSON)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun deleteBorrowNotFoundBorrow(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val book= Book("111","Alicia","555",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","123")
        entityManager.persist(user)
        entityManager.persist(Borrow(14, Date(2020,2,4),book,user))
        val request = MockMvcRequestBuilders
            .delete("/borrows/15")
            .contentType(MediaType.APPLICATION_JSON)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This borrow does not exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }


}