package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import co.edu.eam.disenosoftware.libreria.repositories.PublisherRepository
import javax.persistence.EntityManager

@Service
class BookService {
    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var publisherRepository: PublisherRepository


    fun createBook(book: Book, idPublisher:Int){
        val publisher= publisherRepository.find(idPublisher)
        if(publisher== null){
            throw BusinessException("this publisher does not exist")
        }
        var bookById= bookRepository.find(book.codigo)

        if (bookById!=null){
            throw BusinessException("This book already exist")
        }
        var bookByName= bookRepository.findByName(book.nombre)

        if (bookByName!=null){
            throw BusinessException("This book with this name already exist")
        }
        book.publisher= publisher
        bookRepository.create(book)
    }

    fun returnBook(book: Book){
        var bookById= bookRepository.find(book.codigo)

        if ( bookById == null){
          throw BusinessException("this book is not in the database")
        }

        bookById.stock= bookById.stock+1
        bookRepository.update(book)

    }

    fun editBook(book: Book){
        bookRepository.find(book.codigo)?:throw BusinessException("This book does not exist")
        bookRepository.update(book)
    }

    fun getAllBooks():List<Book>{
        val listBooks= bookRepository.getAllBooks()
        return listBooks
    }


}