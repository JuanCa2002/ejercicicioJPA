package co.edu.eam.disenosoftware.libreria.models.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "autor")
data class Author(
     @Id
     @Column(name = "codigo_autor")
     val id: Int,

     @Column(name= "apellido")
     var lastName: String,

     @Column(name= "nombre")
     var name: String,


     ):Serializable
