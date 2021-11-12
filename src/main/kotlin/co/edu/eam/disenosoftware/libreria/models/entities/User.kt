package co.edu.eam.disenosoftware.libreria.models.entities

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name= "Usuario")
data class User(
    @Column(name= "nombre_usuario")
    var name:String,

    @Column(name= "apellido_usuario")
    var lastName:String,

    @Id
    @Column(name= "user_identification")
    val identification: String

):Serializable
