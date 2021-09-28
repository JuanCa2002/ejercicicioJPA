package co.edu.eam.disenosoftware.libreria.repositories

import co.edu.eam.disenosoftware.libreria.models.Author
import co.edu.eam.disenosoftware.libreria.models.AuthorBook
import co.edu.eam.disenosoftware.libreria.models.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
@Transactional
class AuthorBookRepository {
    @Autowired
    lateinit var em: EntityManager

    fun findByAuthor(id:Int):List<Book>{
        val query= em.createQuery("SELECT AuBo.book FROM AuthorBook AuBo WHERE AuBo.author.id =:idAuthor")
        query.setParameter("idAuthor",id)
        return query.resultList as List<Book>
    }
    fun findByBook(codigo:String):List<Author>{
        val query= em.createQuery("SELECT AuBo.author FROM AuthorBook AuBo WHERE AuBo.book.codigo =:codigoLibro")
        query.setParameter("codigoLibro",codigo)
        return query.resultList as List<Author>
    }

    fun create(authorBook: AuthorBook){
        em.persist(authorBook)
    }

    fun find(id:Int): AuthorBook?{
        return em.find(AuthorBook::class.java,id)
    }

    fun update(authorBook: AuthorBook) {
        em.merge(authorBook)
    }

    fun delete(id: Int) {
        val authorBook= find(id)
        if (authorBook!=null) {
            em.remove(authorBook)
        }
    }
}