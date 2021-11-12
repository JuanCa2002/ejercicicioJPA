package co.edu.eam.disenosoftware.libreria.models.entities

import java.io.Serializable
import javax.persistence.*
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Column
import javax.persistence.Entity

@Entity
@Table( name= "editorial")
data class Publisher(
      @Column(name= "nombre_editorial")
      var name: String,

      @Id
      @Column( name= "codigo_editorial")
      val code: Int,


):Serializable
