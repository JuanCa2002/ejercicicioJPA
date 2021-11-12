package co.edu.eam.disenosoftware.libreria.controllers

import co.edu.eam.disenosoftware.libreria.models.entities.Borrow
import co.edu.eam.disenosoftware.libreria.models.entities.User
import co.edu.eam.disenosoftware.libreria.models.request.BorrowRequest
import co.edu.eam.disenosoftware.libreria.services.BorrowService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController

@RequestMapping("/borrows")
class BorrowController {

    @Autowired
    lateinit var borrowService: BorrowService

    @PostMapping
    fun createBorrowBook(@RequestBody borrowRequest: BorrowRequest){
         borrowService.createBorrow(borrowRequest)
    }

    @DeleteMapping("{id}")
    fun deleteBorrow(@PathVariable("id")idBorrow:Int){
        borrowService.returnBook(idBorrow)
    }



}