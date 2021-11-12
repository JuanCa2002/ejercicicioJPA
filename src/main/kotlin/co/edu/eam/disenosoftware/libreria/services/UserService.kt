package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.repositories.UserRepository
import co.edu.eam.disenosoftware.libreria.models.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var userRepository: UserRepository

    fun createUser(user: User){
        val userById= userRepository.find(user.identification)

        if (userById!= null){
            throw BusinessException("This user already exist")
        }
        userRepository.create(user)
    }

}