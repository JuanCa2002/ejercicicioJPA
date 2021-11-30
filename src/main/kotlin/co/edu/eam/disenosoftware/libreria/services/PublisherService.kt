package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import co.edu.eam.disenosoftware.libreria.repositories.PublisherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PublisherService {
    @Autowired
    lateinit var publisherRepository: PublisherRepository

    fun createPublisher(publisher: Publisher){

        val publisherById= publisherRepository.find(publisher.code)

        if (publisherById != null){
            throw BusinessException("This publisher already exist")
        }
        publisherRepository.create(publisher)
    }
    fun getAllPublishers():List<Publisher>{
        val listPublishers= publisherRepository.getAllPublishers()
        return listPublishers
    }

}