package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.Publisher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class PublisherServiceTest {
    @Autowired
    lateinit var publisherService: PublisherService

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createPublisherAlreadyExist(){
        val publisherOne= Publisher("Norma",14)
        entityManager.persist(publisherOne)
        val publisherTwo= Publisher("Prisma",14)

        try {
            publisherService.createPublisher(publisherTwo)
            Assertions.fail()
        }catch (e: BusinessException){
            Assertions.assertEquals("This publisher already exist",e.message)
        }
    }

    @Test
    fun createPublisherHappyPath(){
        val publisherOne= Publisher("Norma",14)
        entityManager.persist(publisherOne)
        val publisherTwo= Publisher("Prisma",15)

        publisherService.createPublisher(publisherTwo)
        val publisher= entityManager.find(Publisher::class.java,publisherTwo.code)
        Assertions.assertNotNull(publisher)

        Assertions.assertEquals("Prisma",publisher.name)
    }
}