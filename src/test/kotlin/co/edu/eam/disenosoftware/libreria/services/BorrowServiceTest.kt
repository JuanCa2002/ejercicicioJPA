package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.Borrow
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import co.edu.eam.disenosoftware.libreria.models.entities.User
import co.edu.eam.disenosoftware.libreria.models.request.BorrowRequest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class BorrowServiceTest {

    @Autowired
    lateinit var borrowService: BorrowService

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createBorrowOnlyOneCopy(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",1,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        val borrowRequest= BorrowRequest(45,book.codigo,user.identification)

        try {
            borrowService.createBorrow(borrowRequest)
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("Only one copy of the book remains, therefore it cannot be loaned",e.message)
        }
    }

    @Test
    fun createBorrowUserHasFiveLoans(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        val borrowOne= Borrow(45,fecha,book,user)
        entityManager.persist(borrowOne)
        val borrowTwo= Borrow(46,fecha,book,user)
        entityManager.persist(borrowTwo)
        val borrowThree= Borrow(47,fecha,book,user)
        entityManager.persist(borrowThree)
        val borrowFour= Borrow(48,fecha,book,user)
        entityManager.persist(borrowFour)
        val borrowFive= Borrow(49,fecha,book,user)
        entityManager.persist(borrowFive)
        val borrowSix= BorrowRequest(50,book.codigo,user.identification)

        try {
            borrowService.createBorrow(borrowSix)
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("The user already has 5 loans, therefore this cannot be done",e.message)
        }
    }

    @Test
    fun createBorrowUserAlreadyHasLoanWithThisBook(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        val borrowOne= Borrow(45,fecha,book,user)
        entityManager.persist(borrowOne)
        val borrowTwo= BorrowRequest(46,book.codigo,user.identification)

        try {
            borrowService.createBorrow(borrowTwo)
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("the user already made the loan of this book once, therefore the loan cannot be made",e.message)
        }
    }

    @Test
    fun createBorrowHappyPath(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val bookOne= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(bookOne)
        val bookTwo= Book("111","Juego de tronos","444",10,publisher)
        entityManager.persist(bookTwo)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        val borrowOne= Borrow(45,fecha,bookOne,user)
        entityManager.persist(borrowOne)
        val borrowTwo= BorrowRequest(46,bookTwo.codigo,user.identification)

        borrowService.createBorrow(borrowTwo)
        val borrow= entityManager.find(Borrow::class.java,borrowTwo.id)
        val book= entityManager.find(Book::class.java,bookTwo.codigo)
        Assertions.assertNotNull(borrow)
        Assertions.assertEquals(9,book.stock)
        Assertions.assertEquals("Juan",borrow.user?.name)
        Assertions.assertEquals("Juego de tronos",borrow.book?.nombre)


    }

    @Test
    fun findByUserNotExist(){
        try {
            borrowService.findByUser("111")
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("This user does not exist in the system",e.message)
        }
    }

    @Test
    fun findByUserHappyPath(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        val borrow= Borrow(45,fecha,book,user)
        entityManager.persist(borrow)

        val listBorrowByUser= borrowService.findByUser("111")
        Assertions.assertEquals(1,listBorrowByUser.size)
    }

    @Test
    fun findUserByBookNotExist(){
        try {
            borrowService.findUserByBook("111")
            Assertions.fail()
        }catch (e:BusinessException){
            Assertions.assertEquals("This book does not exist in the system",e.message)
        }
    }

    @Test
    fun findUserByBookHappyPath(){
        val fecha= Date(2019,2,24)
        val publisher= Publisher("Norma",14)
        entityManager.persist(publisher)
        val book= Book("123","Harry Poter","444",10,publisher)
        entityManager.persist(book)
        val user= User("Juan","Torres","111")
        entityManager.persist(user)
        val borrow= Borrow(45,fecha,book,user)
        entityManager.persist(borrow)

        val listBorrowByUser= borrowService.findUserByBook("123")
        Assertions.assertEquals(1,listBorrowByUser.size)
    }
}