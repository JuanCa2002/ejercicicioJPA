package co.edu.eam.disenosoftware.libreria.models.entities

import java.io.Serializable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name= "libro_autor")
data class AuthorBook(
    @Id
    @Column(name= "id")
    val id:Int,

    @ManyToOne
    @JoinColumn(name= "id_libro")
    val book: Book,

    @ManyToOne
    @JoinColumn(name= "id_autor")
    var author: Author,

    ):Serializable
