package co.edu.eam.disenosoftware.libreria.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import co.edu.eam.disenosoftware.libreria.models.entities.Book
import co.edu.eam.disenosoftware.libreria.models.entities.User
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

    fun getAllBooks():List<Book>{
        val query= em.createQuery("SELECT boo FROM Book boo")
        return query.resultList as List<Book>
    }

    fun create(book: Book){
       em.persist(book)
    }

    fun find(codigo:String?): Book?{
       return em.find(Book::class.java,codigo)
    }
    fun findByName(name: String): Book? {
        val query = em.createQuery("SELECT book FROM Book book WHERE book.nombre = :nombre")
        query.setParameter("nombre", name)

        val list = query.resultList as List<Book>

        //asignacion condicional
        return if (list.isEmpty()) null else list[0]
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