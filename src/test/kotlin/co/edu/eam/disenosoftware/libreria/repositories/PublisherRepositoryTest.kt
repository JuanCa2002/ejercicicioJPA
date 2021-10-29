package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class PublisherRepositoryTest {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var publisherRepository: PublisherRepository

    @Test
    fun createTest(){
        publisherRepository.create(Publisher("Norma",14))
        val publisher= entityManager.find(Publisher::class.java,14)
        Assertions.assertNotNull(publisher)
        Assertions.assertEquals("Norma",publisher.name)
    }

    @Test
    fun findTest(){
        entityManager.persist(Publisher("Norma",14))
        val publisher= publisherRepository.find(14)
        Assertions.assertNotNull(publisher)
        Assertions.assertEquals("Norma",publisher?.name)
    }

    @Test
    fun deleteTest(){
        entityManager.persist(Publisher("Norma",14))
        publisherRepository.delete(14)
        val publisher= entityManager.find(Publisher::class.java,14)
        Assertions.assertNull(publisher)
    }
    @Test
    fun updateTest(){
        entityManager.persist(Publisher("Norma",14))
        entityManager.flush()

        val publisher= entityManager.find(Publisher::class.java,14)
        publisher.name="Prisma"

        entityManager.clear()
        publisherRepository.update(publisher)
        val publisherToAssert= entityManager.find(Publisher::class.java,14)
        Assertions.assertEquals("Prisma",publisherToAssert.name)
    }
}