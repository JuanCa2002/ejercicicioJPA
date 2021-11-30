package co.edu.eam.disenosoftware.libreria.controllers

import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import co.edu.eam.disenosoftware.libreria.services.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController


@RequestMapping("/publishers")
class PublisherController {

    @Autowired
    lateinit var publisherService: PublisherService

    @PostMapping
    fun createPublisher(@RequestBody  publisher: Publisher){
         publisherService.createPublisher(publisher)
    }

    @GetMapping
    fun getAllPublishers():List<Publisher>{
        val listPublishers=publisherService.getAllPublishers()
        return listPublishers
    }
}