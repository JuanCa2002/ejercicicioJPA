package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class PublisherRepository {
    @Autowired
    lateinit var em: EntityManager

    fun create(publisher: Publisher){
        em.persist(publisher)
    }

    fun find(code:Int): Publisher?{
        return em.find(Publisher::class.java,code)
    }

    fun update(publisher: Publisher) {
        em.merge(publisher)
    }

    fun delete(code: Int) {
        val publisher = find(code)
        if (publisher!=null) {
            em.remove(publisher)
        }
    }
}