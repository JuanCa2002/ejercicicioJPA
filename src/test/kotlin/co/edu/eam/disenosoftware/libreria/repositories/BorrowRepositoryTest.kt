package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.Book
import co.edu.eam.disenosoftware.libreria.models.Borrow
import co.edu.eam.disenosoftware.libreria.models.Publisher
import co.edu.eam.disenosoftware.libreria.models.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class BorrowRepositoryTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var borrowRepository: BorrowRepository

    @Test
    fun createTest(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        borrowRepository.create(Borrow(45,fecha,book,user))
        val borrow= entityManager.find(Borrow::class.java,45)
        Assertions.assertNotNull(borrow)
        val fechaAssert= Date(2019,2,24)
        Assertions.assertEquals(fechaAssert,borrow.dateTime)
        Assertions.assertEquals("Harry Poter",borrow.book.nombre)
        Assertions.assertEquals("444",borrow.book.isbn)
        Assertions.assertEquals("Norma",borrow.book.publisher.name)
        Assertions.assertEquals("Juan",borrow.user.name)
        Assertions.assertEquals("Torres",borrow.user.lastName)
    }
    @Test
    fun findTest(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",11,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        entityManager.persist(Borrow(45,fecha,book,user))
        val borrow= borrowRepository.find(45)
        Assertions.assertNotNull(publisher)
        Assertions.assertNotNull(borrow)
        val fechaAssert= Date(2019,2,24)
        Assertions.assertEquals(fechaAssert,borrow?.dateTime)
        Assertions.assertEquals("Harry Poter",borrow?.book?.nombre)
        Assertions.assertEquals("444",borrow?.book?.isbn)
        Assertions.assertEquals("Norma",borrow?.book?.publisher?.name)
        Assertions.assertEquals("Juan",borrow?.user?.name)
        Assertions.assertEquals("Torres",borrow?.user?.lastName)
    }

    @Test
    fun deleteTest(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",12,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        entityManager.persist(Borrow(45,fecha,book,user))
        borrowRepository.delete(45)
        val borrow= entityManager.find(Borrow::class.java,45)
        Assertions.assertNull(borrow)
    }
    @Test
    fun updateTest(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",14,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        entityManager.persist(Borrow(45,fecha,book,user))
        entityManager.flush()

        val borrow= entityManager.find(Borrow::class.java,45)
        borrow.dateTime= Date(2020,4,25)

        entityManager.clear()
        borrowRepository.update(borrow)
        val borrowToAssert= entityManager.find(Borrow::class.java,45)
        Assertions.assertEquals(Date(2020,4,25),borrowToAssert.dateTime)
    }
    @Test
    fun findByUserTest(){
        val userOne= User("Juana","Sanchez","111")
        entityManager.persist(userOne)
        val userTwo= User("Pedro","Juarez","888")
        entityManager.persist(userTwo)
        val publisher=Publisher("Norma",14)
        entityManager.persist(publisher)
        val bookOne= Book("123","Harry Potter","444",20,publisher)
        entityManager.persist(bookOne)
        val bookTwo= Book("421","Alicia en el pais de la marav","555",14,publisher)
        entityManager.persist(bookTwo)

        entityManager.persist(Borrow(1,Date(2020,2,4),bookTwo,userOne))
        entityManager.persist(Borrow(2,Date(2020,2,4),bookOne,userOne))
        entityManager.persist(Borrow(3,Date(2020,2,4),bookOne,userTwo))

        val listOne=borrowRepository.findByUser("111")
        val listTwo= borrowRepository.findByUser("888")

        Assertions.assertEquals(2,listOne.size)
        Assertions.assertEquals(1,listTwo.size)
        listOne.forEach {Assertions.assertEquals("111",it.user.identification) }
        listTwo.forEach {Assertions.assertEquals("888",it.user.identification) }
    }

    @Test
    fun findByBookTest(){
        val userOne= User("Juana","Sanchez","111")
        entityManager.persist(userOne)
        val userTwo= User("Pedro","Juarez","888")
        entityManager.persist(userTwo)
        val publisher=Publisher("Norma",14)
        entityManager.persist(publisher)
        val bookOne= Book("123","Harry Potter","444",15,publisher)
        entityManager.persist(bookOne)
        val bookTwo= Book("421","Alicia en el pais de la maravillas","555",22,publisher)
        entityManager.persist(bookTwo)
        val bookThree= Book("821","Los juegos del hambre","666",16,publisher)
        entityManager.persist(bookThree)

        entityManager.persist(Borrow(1,Date(2020,2,4),bookTwo,userOne))
        entityManager.persist(Borrow(2,Date(2020,2,4),bookOne,userOne))
        entityManager.persist(Borrow(3,Date(2020,2,4),bookOne,userTwo))
        entityManager.persist(Borrow(4,Date(2020,2,4),bookThree,userTwo))

        val listOne=borrowRepository.findByBook("123")
        val listTwo= borrowRepository.findByBook("421")
        val listThree= borrowRepository.findByBook("821")

        Assertions.assertEquals(2,listOne.size)
        Assertions.assertEquals(1,listTwo.size)
        Assertions.assertEquals(1,listThree.size)
        listOne.forEach {Assertions.assertEquals("123",it.book.codigo) }
        listTwo.forEach {Assertions.assertEquals("421",it.book.codigo) }
        listThree.forEach {Assertions.assertEquals("821",it.book.codigo) }
    }

}