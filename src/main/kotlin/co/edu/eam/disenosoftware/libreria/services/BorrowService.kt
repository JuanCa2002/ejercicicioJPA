package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.Book
import co.edu.eam.disenosoftware.libreria.models.Borrow
import co.edu.eam.disenosoftware.libreria.models.User
import co.edu.eam.disenosoftware.libreria.repositories.BorrowRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityManager

@Service
class BorrowService {

    @Autowired
    lateinit var borrowRepository: BorrowRepository

    @Autowired
    lateinit var entityManager: EntityManager

    fun createBorrow(borrow: Borrow){

        if (borrow.book.stock == 1){
            throw BusinessException("Only one copy of the book remains, therefore it cannot be loaned")
        }

        var listBorrowsByUser= borrowRepository.findByUser(borrow.user.identification)

        if(listBorrowsByUser.size==5){
          throw BusinessException("The user already has 5 loans, therefore this cannot be done")
        }

        listBorrowsByUser.forEach { if(it.book.codigo== borrow.book.codigo){
            throw BusinessException("the user already made the loan of this book once, therefore the loan cannot be made")
        }
        }
        var bookUpdate= entityManager.find(Book::class.java,borrow.book.codigo)
        bookUpdate.stock= bookUpdate.stock-1
        entityManager.merge(bookUpdate)
        borrowRepository.create(borrow)
    }

    fun findByUser(idUser: String):List<Borrow>{

        var userById= entityManager.find(User::class.java,idUser)

        if (userById== null){
            throw BusinessException("This user does not exist in the system")
        }

        var listBorrowsByUser= borrowRepository.findByUser(idUser)
        return listBorrowsByUser

    }

    fun findUserByBook(codeBook: String): List<User>{
        var bookById= entityManager.find(Book::class.java,codeBook)

        if (bookById == null){
            throw BusinessException("This book does not exist in the system")
        }

        var listUsersByBook= borrowRepository.findUserByBook(codeBook)
        return listUsersByBook
    }


}