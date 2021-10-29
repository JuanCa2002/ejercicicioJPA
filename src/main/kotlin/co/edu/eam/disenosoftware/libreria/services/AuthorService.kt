package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.entities.Author
import co.edu.eam.disenosoftware.libreria.repositories.AuthorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AuthorService {
    @Autowired
    lateinit var authorRepository: AuthorRepository

    fun createAuthor(author: Author){
        val authorById= authorRepository.find(author.id)

        if (authorById!= null){
            throw BusinessException("This author already exist")
        }
        authorRepository.create(author)
    }
}