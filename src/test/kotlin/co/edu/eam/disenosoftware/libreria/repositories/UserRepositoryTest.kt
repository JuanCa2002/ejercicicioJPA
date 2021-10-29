package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.User
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
class UserRepositoryTest {
    @Autowired
    lateinit var entityManager: EntityManager

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun createTest(){
        userRepository.create(User("Julian","Salcedo","111"))
        val user= entityManager.find(User::class.java,"111")
        Assertions.assertNotNull(user)
        Assertions.assertEquals("Julian",user.name)
        Assertions.assertEquals("Salcedo",user.lastName)

    }

    @Test
    fun findTest(){

        entityManager.persist(User("Julian","Salcedo","111"))
        val user= userRepository.find("111")
        Assertions.assertNotNull(user)
        Assertions.assertEquals("Julian",user?.name)
        Assertions.assertEquals("Salcedo",user?.lastName)
    }

    @Test
    fun deleteTest(){
        entityManager.persist(User("Julian","Salcedo","111"))
        userRepository.delete("111")
        val user= entityManager.find(User::class.java,"111")
        Assertions.assertNull(user)
    }
    @Test
    fun updateTest(){
        entityManager.persist(User("Julian","Salcedo","111"))
        entityManager.flush()

        val user= entityManager.find(User::class.java,"111")
        user.name="Jose"
        user.lastName="Beltran"

        entityManager.clear()
        userRepository.update(user)
        val userToAssert= entityManager.find(User::class.java,"111")
        Assertions.assertEquals("Jose",userToAssert.name)
        Assertions.assertEquals("Beltran",userToAssert.lastName)
    }
}