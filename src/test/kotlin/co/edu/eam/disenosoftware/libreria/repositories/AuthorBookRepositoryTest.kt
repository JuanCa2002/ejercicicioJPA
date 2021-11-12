package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.AuthorBook
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import co.edu.eam.disenosoftware.libreria.models.entities.Author
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class AuthorBookRepositoryTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var authorBookRepository:  AuthorBookRepository

    @Test
    fun createTest(){
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val author= Author(12,"Torres","Juan")
        entityManager.persist(author)
        authorBookRepository.create(AuthorBook(45,book,author))
        val authorBook= entityManager.find(AuthorBook::class.java,45)
        Assertions.assertNotNull(authorBook)
        Assertions.assertEquals("Harry Poter",authorBook.book.nombre)
        Assertions.assertEquals("444",authorBook.book.isbn)
        Assertions.assertEquals("Norma",authorBook.book.publisher?.name)
        Assertions.assertEquals("Juan",authorBook.author.name)
        Assertions.assertEquals("Torres",authorBook.author.lastName)
    }
    @Test
    fun findTest(){
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val author= Author(12,"Torres","Juan")
        entityManager.persist(author)
        entityManager.persist(AuthorBook(45,book,author))
        val authorBook= authorBookRepository.find(45)
        Assertions.assertNotNull(authorBook)
        Assertions.assertEquals("Harry Poter",authorBook?.book?.nombre)
        Assertions.assertEquals("444",authorBook?.book?.isbn)
        Assertions.assertEquals("Norma",authorBook?.book?.publisher?.name)
        Assertions.assertEquals("Juan",authorBook?.author?.name)
        Assertions.assertEquals("Torres",authorBook?.author?.lastName)
    }

    @Test
    fun deleteTest(){
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val author= Author(12,"Torres","Juan")
        entityManager.persist(author)
        entityManager.persist(AuthorBook(45,book,author))
        authorBookRepository.delete(45)
        val authorBook= entityManager.find(AuthorBook::class.java,45)
        Assertions.assertNull(authorBook)
    }
    @Test
    fun updateTest(){
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val author= Author(12,"Torres","Juan")
        entityManager.persist(author)
        entityManager.persist(AuthorBook(45,book,author))
        val newAuthor= Author(13,"Beltran","Sara")
        entityManager.persist(newAuthor)
        entityManager.flush()
        val authorBook= entityManager.find(AuthorBook::class.java,45)
        authorBook.author= newAuthor

        entityManager.clear()
        authorBookRepository.update(authorBook)


        val authorBookToAssert= entityManager.find(AuthorBook::class.java,45)
        Assertions.assertEquals("Beltran",authorBookToAssert.author.lastName)
        Assertions.assertEquals("Sara",authorBookToAssert.author.name)


    }
    @Test
    fun findByAuthorTest(){
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val bookOne= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(bookOne)
        val bookTwo= Book("421","Alicia en el pais de las maravillas","555",20,publisher)
        entityManager.persist(bookTwo)
        val bookThree= Book("521","Los juegos del hambre","666",12,publisher)
        entityManager.persist(bookThree)
        val authorOne= Author(12,"Torres","Juan")
        entityManager.persist(authorOne)
        val authorTwo= Author(13,"Linares","Jose")
        entityManager.persist(authorTwo)
        entityManager.persist(AuthorBook(1,bookOne,authorOne))
        entityManager.persist(AuthorBook(2,bookTwo,authorTwo))
        entityManager.persist(AuthorBook(3,bookThree,authorTwo))

        val listOne= authorBookRepository.findByAuthor(12)
        print(listOne)
        val listTwo= authorBookRepository.findByAuthor(13)
        print(listTwo)

        Assertions.assertEquals(1,listOne.size)
        Assertions.assertEquals("123",listOne[0].codigo)
        Assertions.assertEquals(2,listTwo.size)
        Assertions.assertEquals("421",listTwo[0].codigo)
        Assertions.assertEquals("521",listTwo[1].codigo)

    }

    @Test
    fun findByBookTest(){
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val bookOne= Book("123","Harry Poter","444",5,publisher)
        entityManager.persist(bookOne)
        val bookTwo= Book("421","Alicia en el pais de las maravillas","555",6,publisher)
        entityManager.persist(bookTwo)
        val bookThree= Book("521","Los juegos del hambre","666",8,publisher)
        entityManager.persist(bookThree)
        val authorOne= Author(12,"Torres","Juan")
        entityManager.persist(authorOne)
        val authorTwo= Author(13,"Linares","Jose")
        entityManager.persist(authorTwo)
        entityManager.persist(AuthorBook(1,bookOne,authorOne))
        entityManager.persist(AuthorBook(2,bookOne,authorTwo))
        entityManager.persist(AuthorBook(3,bookThree,authorTwo))

        val listOne= authorBookRepository.findByBook("123")
        val listTwo= authorBookRepository.findByBook("521")

        Assertions.assertEquals(2,listOne.size)
        Assertions.assertEquals(12,listOne[0].id)
        Assertions.assertEquals(13,listOne[1].id)
        Assertions.assertEquals(1,listTwo.size)
        Assertions.assertEquals(13,listTwo[0].id)
    }
}