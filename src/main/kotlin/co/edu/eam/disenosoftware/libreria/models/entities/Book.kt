package co.edu.eam.disenosoftware.libreria.models.entities
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne



@Entity
@Table(name="libro")
data class Book(
    @Id
    @Column(name = "codigo_libro")
    var codigo: String?=" ",

    @Column(name= "nombre_libro")
    var nombre: String,

    @Column(name= "isbn_libro")
    var isbn: String,

    @Column(name= "cantidad")
    var stock: Int,

    @ManyToOne
    @JoinColumn(name= "id_editorial")
    var publisher: Publisher?,


    ):Serializable


