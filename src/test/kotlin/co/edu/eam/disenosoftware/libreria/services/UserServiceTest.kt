package co.edu.eam.disenosoftware.libreria.services

import co.edu.eam.disenosoftware.libreria.exceptions.BusinessException
import co.edu.eam.disenosoftware.libreria.models.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var entityManager: EntityManager

    @Test
    fun createUserAlreadyExist(){
        val userOne= User("Julian","Salcedo","111")
        entityManager.persist(userOne)
        val userTwo= User("Sara","Martinez","111")

        try {
            userService.createUser(userTwo)
            Assertions.fail()
        }catch (e: BusinessException){
            Assertions.assertEquals("This user already exist",e.message)
        }

    }

    @Test
    fun createUserHappyPath(){
        val userOne= User("Julian","Salcedo","111")
        entityManager.persist(userOne)
        val userTwo= User("Sara","Martinez","222")

        userService.createUser(userTwo)
        val user= entityManager.find(User::class.java,userTwo.identification)
        Assertions.assertNotNull(user)
        Assertions.assertEquals("Sara",user.name)
        Assertions.assertEquals("Martinez",user.lastName)
    }
}