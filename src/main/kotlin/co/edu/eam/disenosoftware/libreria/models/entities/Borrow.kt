package co.edu.eam.disenosoftware.libreria.models.entities

import java.io.Serializable
import java.util.*
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name= "prestamo")
data class Borrow(
    @Id
     @Column(name= "id")
     val id: Int,

    @Column(name= "fecha_prestamo")
     var dateTime: Date,

    @ManyToOne
     @JoinColumn(name= "id_libro")
     var book: Book?,

    @ManyToOne
     @JoinColumn(name= "id_usuario")
     var user: User?,


    ):Serializable
