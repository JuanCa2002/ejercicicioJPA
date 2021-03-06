package co.edu.eam.disenosoftware.libreria.controllers

import co.edu.eam.disenosoftware.libreria.models.entities.Borrow
import co.edu.eam.disenosoftware.libreria.models.entities.User
import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.services.BorrowService
import co.edu.eam.disenosoftware.libreria.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.persistence.EntityNotFoundException

@RestController

@RequestMapping("/users")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var borrowService: BorrowService

    @PostMapping
    fun createUser(@RequestBody user: User){
        userService.createUser(user)
    }

    @GetMapping("/{id}/borrows")
    fun findBorrowsByUser(@PathVariable("id")idUsuario: String):List<Borrow>{
        val listBorrows=borrowService.findByUser(idUsuario)
        return listBorrows
    }

    @GetMapping("/{id}/books")
    fun findBooksByUser(@PathVariable("id")idUsuario: String):List<Book>{
        val listBooks=borrowService.findBookByUser(idUsuario)
        return listBooks
    }

    @GetMapping
    fun getAllUsers():List<User>{
        val listUsers= userService.getAllUsers()
        return listUsers
    }




}