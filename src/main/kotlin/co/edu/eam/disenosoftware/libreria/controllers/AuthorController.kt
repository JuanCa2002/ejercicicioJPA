package co.edu.eam.disenosoftware.libreria.controllers


import co.edu.eam.disenosoftware.libreria.models.entities.Author
import co.edu.eam.disenosoftware.libreria.services.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController


@RequestMapping("/authors")

class AuthorController {

    @Autowired
    lateinit var authorService: AuthorService

    @PostMapping
    fun createAuthor(@RequestBody  author: Author){
        authorService.createAuthor(author)
    }
}