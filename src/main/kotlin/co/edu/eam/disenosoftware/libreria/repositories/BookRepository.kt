package co.edu.eam.disenosoftware.libreria.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import co.edu.eam.disenosoftware.libreria.models.Book
import java.util.concurrent.Flow
import javax.persistence.EntityManager

@Component
@Transactional
class BookRepository {

    @Autowired
    lateinit var em:EntityManager

    fun findByPublisher(code:Int):List<Book>{
       val query= em.createQuery("SELECT boo FROM Book boo WHERE boo.publisher.code=:codePublisher")
       query.setParameter("codePublisher",code)
        return query.resultList as List<Book>
    }

    fun create(book:Book){
       em.persist(book)
    }

    fun find(codigo:String):Book?{
       return em.find(Book::class.java,codigo)
    }

    fun update(book: Book) {
        em.merge(book)
    }

    fun delete(codigo: String) {
        val book = find(codigo)
        if (book!=null) {
            em.remove(book)
        }
    }
}