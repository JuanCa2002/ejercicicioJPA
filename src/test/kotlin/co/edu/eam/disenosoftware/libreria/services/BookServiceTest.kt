package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.Book
import co.edu.eam.disenosoftware.libreria.models.Publisher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class BookServiceTest {

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createBookAlreadyExistById(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        val bookOne=Book("123","Harry Poter","444",12,publisher)
        entityManager.persist(bookOne)
        val bookTwo=Book("123","Juego de tronos","666",11,publisher)
        try {
            bookService.createBook(bookTwo)
            Assertions.fail()
        }catch (e: BusinessException){
            Assertions.assertEquals("This book already exist",e.message)
        }
    }

    @Test
    fun createBookAlreadyExistByName(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        val bookOne=Book("123","Harry Poter","444",12,publisher)
        entityManager.persist(bookOne)
        val bookTwo=Book("222","Harry Poter","666",11,publisher)
        try {
            bookService.createBook(bookTwo)
            Assertions.fail()
        }catch (e: BusinessException){
            Assertions.assertEquals("This book with this name already exist",e.message)
        }
    }

    @Test
    fun createBookHappyPath(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        val bookOne=Book("123","Harry Poter","444",12,publisher)
        entityManager.persist(bookOne)
        val bookTwo=Book("222","Juego de tronos","666",11,publisher)

        bookService.createBook(bookTwo)
        val book= entityManager.find(Book::class.java,bookTwo.codigo)
        Assertions.assertNotNull(book)
        Assertions.assertEquals("Juego de tronos",book.nombre)
        Assertions.assertEquals("666",book.isbn)
        Assertions.assertEquals("Norma",book.publisher.name)


    }

    @Test
    fun returnBookThisBookNotExist(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        val bookOne=Book("123","Harry Poter","444",12,publisher)
        try {
            bookService.returnBook(bookOne)
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("this book is not in the database",e.message)
        }
    }

    @Test
    fun returnBookHappyPath(){
        val publisher=  Publisher("Norma",12)
        entityManager.persist(publisher)
        val bookOne=Book("123","Harry Poter","444",12,publisher)
        entityManager.persist(bookOne)

        bookService.returnBook(bookOne)
        val book= entityManager.find(Book::class.java,bookOne.codigo)
        Assertions.assertEquals(13,book.stock)

    }
}