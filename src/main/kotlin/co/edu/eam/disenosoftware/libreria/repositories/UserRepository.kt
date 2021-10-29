package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class UserRepository {
    @Autowired
    lateinit var em: EntityManager

    fun create(user: User){
        em.persist(user)
    }

    fun find(identification:String?): User?{
        return em.find(User::class.java,identification)
    }

    fun update(user: User) {
        em.merge(user)
    }

    fun delete(identification:String) {
        val user = find(identification)
        if (user!=null) {
            em.remove(user)
        }
    }
}