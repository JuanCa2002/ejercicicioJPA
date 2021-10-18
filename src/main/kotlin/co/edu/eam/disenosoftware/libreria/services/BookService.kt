package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.repositories.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import co.edu.eam.disenosoftware.libreria.models.Book

@Service
class BookService {
    @Autowired
    lateinit var bookRepository: BookRepository

    fun createBook(book: Book){
        var bookById= bookRepository.find(book.codigo)

        if (bookById!=null){
            throw BusinessException("This book already exist")
        }
        var bookByName= bookRepository.findByName(book.nombre)

        if (bookByName!=null){
            throw BusinessException("This book with this name already exist")
        }
        bookRepository.create(book)
    }

    fun returnBook(book:Book){
        var bookById= bookRepository.find(book.codigo)

        if ( bookById == null){
          throw BusinessException("this book is not in the database")
        }

        bookById.stock= bookById.stock+1
        bookRepository.update(book)

    }


}