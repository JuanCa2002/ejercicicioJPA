package co.edu.eam.disenosoftware.libreria.controllers

import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
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
import javax.persistence.EntityManager

@SpringBootTest
@Transactional

@AutoConfigureMockMvc
class PublisherControllerTest {

    @Autowired
    lateinit var mocMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createPublisherHappyPath(){
        val body = """
           {
            "name": "Norma",
            "code":  1
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/publishers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(200, resp.status)
    }

    @Test
    fun createPublisherAlreadyExist(){
        val publisher= Publisher("Norma",1)
        entityManager.persist(publisher)
        val body = """
           {
            "name": "Norma",
            "code":  1
            }
        """.trimIndent()
        val request = MockMvcRequestBuilders
            .post("/publishers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
        val response = mocMvc.perform(request)
        val resp = response.andReturn().response
        Assertions.assertEquals(412, resp.status)
        Assertions.assertEquals("{\"message\":\"This publisher already exist\",\"code\":412}".trimIndent(),
            resp.contentAsString)
    }
}