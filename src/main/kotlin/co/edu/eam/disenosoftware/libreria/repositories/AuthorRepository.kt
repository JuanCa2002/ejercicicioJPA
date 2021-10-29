package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Author
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class AuthorRepository {
    @Autowired
    lateinit var em: EntityManager

    fun create(author: Author){
        em.persist(author)
    }

    fun find(id:Int): Author?{
        return em.find(Author::class.java,id)
    }

    fun update(author: Author) {
        em.merge(author)
    }

    fun delete(id: Int) {
        val author = find(id)
        if (author!=null) {
            em.remove(author)
        }
    }

}