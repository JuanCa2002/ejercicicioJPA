package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Author
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class AuthorRepositoryTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @Test
    fun createTest(){
        authorRepository.create(Author(1,"Torres","Juan"))
        val author= entityManager.find(Author::class.java,1)
        Assertions.assertNotNull(author)
        Assertions.assertEquals("Torres",author.lastName)
        Assertions.assertEquals("Juan",author.name)
    }

    @Test
    fun findTest(){

        entityManager.persist(Author(1,"Torres","Juan"))
        val author= authorRepository.find(1)
        Assertions.assertNotNull(author)
        Assertions.assertEquals("Torres",author?.lastName)
        Assertions.assertEquals("Juan",author?.name)
    }

    @Test
    fun deleteTest(){
        entityManager.persist(Author(1,"Torres","Juan"))
        authorRepository.delete(1)
        val author= entityManager.find(Author::class.java,1)
        Assertions.assertNull(author)
    }
    @Test
    fun updateTest(){
        entityManager.persist(Author(1,"Torres","Juan"))
        entityManager.flush()

        val author= entityManager.find(Author::class.java,1)
        author.name="Jose"
        author.lastName="Beltran"

        entityManager.clear()
        authorRepository.update(author)
        val authorToAssert= entityManager.find(Author::class.java,1)
        Assertions.assertEquals("Jose",authorToAssert.name)
        Assertions.assertEquals("Beltran",authorToAssert.lastName)
    }
}