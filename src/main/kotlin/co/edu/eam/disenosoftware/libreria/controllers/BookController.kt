package co.edu.eam.disenosoftware.libreria.controllers

import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.User
import co.edu.eam.disenosoftware.libreria.services.BookService
import co.edu.eam.disenosoftware.libreria.services.BorrowService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController

@RequestMapping("/books")
class BookController {

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var borrowService:BorrowService

    @PostMapping("/{code}")
    fun createBook(@PathVariable("code")idPublisher:Int ,@Validated @RequestBody book: Book){
        bookService.createBook(book,idPublisher)
    }

    @PutMapping("/{codigo}")
    fun editBook(@PathVariable("codigo") codeBook:String, @RequestBody book: Book){
        book.codigo= codeBook
        bookService.editBook(book)
    }
    @GetMapping("/{id}/users")
    fun findUsersByBook(@PathVariable("id")idBook: String):List<User>{
        val listUsers= borrowService.findUserByBook(idBook)
        return listUsers
    }
}