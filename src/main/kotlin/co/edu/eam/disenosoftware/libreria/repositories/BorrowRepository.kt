package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.entities.Borrow
import co.edu.eam.disenosoftware.libreria.models.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class BorrowRepository {
    @Autowired
    lateinit var em: EntityManager

    fun findByUser(identification:String):List<Borrow>{
        val query= em.createQuery("SELECT bor FROM Borrow bor WHERE bor.user.identification =:userIdentification")
        query.setParameter("userIdentification",identification)
        return query.resultList as List<Borrow>
    }
    fun findByBook(codigo:String):List<Borrow>{
        val query= em.createQuery("SELECT bor FROM Borrow bor WHERE bor.book.codigo =:codigoLibro")
        query.setParameter("codigoLibro",codigo)
        return query.resultList as List<Borrow>
    }

    fun findUserByBook(codigo:String):List<User>{
        val query= em.createQuery("SELECT bor.user FROM Borrow bor WHERE bor.book.codigo =: codigoLibro")
        query.setParameter("codigoLibro",codigo)
        return query.resultList as List<User>
    }

    fun create(borrow: Borrow){
        em.persist(borrow)
    }


    fun find(id:Int): Borrow?{
        return em.find(Borrow::class.java,id)
    }

    fun update(borrow: Borrow) {
        em.merge(borrow)
    }

    fun delete(id: Int) {
        val borrow = find(id)
        if (borrow!=null) {
            em.remove(borrow
            )
        }
    }
}