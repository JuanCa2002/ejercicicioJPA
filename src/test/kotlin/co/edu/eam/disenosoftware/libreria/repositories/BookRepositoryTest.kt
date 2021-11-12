package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class BookRepositoryTest {

    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var bookRepository: BookRepository

    @Test
    fun createTest(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        bookRepository.create(Book("123","Harry Poter","444",12,publisher))
        val book = entityManager.find(Book::class.java,  "123")
        Assertions.assertNotNull(book)
        Assertions.assertEquals("Harry Poter", book.nombre)
        Assertions.assertEquals("444", book.isbn)
        Assertions.assertEquals("Norma", book.publisher?.name)
        Assertions.assertEquals(12, book.publisher?.code)
    }

    @Test
    fun findTest() {
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        entityManager.persist(Book("123","Harry Poter","444",11,publisher))

        val book = bookRepository.find("123")

        Assertions.assertNotNull(book)
        Assertions.assertEquals("Harry Poter", book?.nombre)
        Assertions.assertEquals("444", book?.isbn)
        Assertions.assertEquals("Norma", book?.publisher?.name)
        Assertions.assertEquals(12, book?.publisher?.code)
    }

    @Test
    fun testDelete(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        entityManager.persist(Book("123","Harry Poter","444",9,publisher))


        bookRepository.delete("123")


        val book = entityManager.find(Book::class.java, "123")
        Assertions.assertNull(book)
    }

    @Test
    fun testUpdate() {
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        entityManager.persist(Book("123","Harry Poter","444",8,publisher))
        entityManager.flush()

        //ejecutando...
        val book = entityManager.find(Book::class.java, "123")
        book.nombre = "Alicia en el pais de la maravillas"
        book.isbn = "555"

        entityManager.clear()
        bookRepository.update(book)

        //assersiones
        val bookToAssert = entityManager.find(Book::class.java, "123")
        Assertions.assertEquals("Alicia en el pais de la maravillas", bookToAssert.nombre)
        Assertions.assertEquals("555", bookToAssert.isbn)

    }
    @Test
    fun findByPublisherTest(){
        val publisherOne= Publisher("Norma",1)
        entityManager.persist(publisherOne)
        val publisherTwo= Publisher("Prisma",2)
        entityManager.persist(publisherTwo)
        entityManager.persist(Book("111","Harry Potter","444",2,publisherOne))
        entityManager.persist(Book("222","Alicia en el pais de las maravillas","555",5,publisherOne))
        entityManager.persist((Book("333","Los juegos del hambre","666",10,publisherTwo)))

        val listOne= bookRepository.findByPublisher(1)
        val listTwo= bookRepository.findByPublisher(2)
        Assertions.assertEquals(2,listOne.size)
        Assertions.assertEquals(1,listTwo.size)
        listOne.forEach {Assertions.assertEquals(1,it.publisher?.code) }
        listTwo.forEach {Assertions.assertEquals(2,it.publisher?.code) }
    }

}