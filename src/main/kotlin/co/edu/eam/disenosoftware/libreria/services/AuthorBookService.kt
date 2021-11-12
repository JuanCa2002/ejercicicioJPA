package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.entities.AuthorBook
import co.edu.eam.disenosoftware.libreria.repositories.AuthorBookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorBookService {
    @Autowired
    lateinit var authorBookRepository: AuthorBookRepository

    fun createAuthorBook(authorBook: AuthorBook){
        var listAuthorBookByBook= authorBookRepository.findByBook(authorBook.book.codigo)

        listAuthorBookByBook.forEach { if(it.id == authorBook.author.id){
            throw BusinessException("This book already has this author")
        }
        }
        authorBookRepository.create(authorBook)
    }
}