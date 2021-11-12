package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.entities.Author
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class AuthorServiceTest {
    @Autowired
    lateinit var authorService: AuthorService

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createAuthorAlreadyExist(){
        val authorOne= Author(1,"Torres","Juan")
        entityManager.persist(authorOne)
        val authorTwo= Author(1,"Beltran","Sandra")
        try {
            authorService.createAuthor(authorTwo)
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("This author already exist",e.message)
        }
    }

    @Test
    fun createAuthorHappyPath(){
        val authorOne= Author(1,"Torres","Juan")
        entityManager.persist(authorOne)
        val authorTwo= Author(2,"Beltran","Sandra")
        authorService.createAuthor(authorTwo)

        val author= entityManager.find(Author::class.java,authorTwo.id)
        Assertions.assertNotNull(author)
        Assertions.assertEquals("Sandra",author.name)
        Assertions.assertEquals("Beltran",author.lastName)
    }
}