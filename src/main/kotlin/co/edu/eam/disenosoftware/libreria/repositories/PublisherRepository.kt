package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Borrow
import co.edu.eam.disenosoftware.libreria.models.entities.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class PublisherRepository {
    @Autowired
    lateinit var em: EntityManager

    fun getAllPublishers():List<Publisher>{
        val query= em.createQuery("SELECT pub FROM Publisher pub")
        return query.resultList as List<Publisher>
    }

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