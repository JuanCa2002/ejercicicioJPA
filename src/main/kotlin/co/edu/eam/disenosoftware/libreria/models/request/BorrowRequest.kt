package co.edu.eam.disenosoftware.libreria.models.request

data class BorrowRequest(
    val id: Int,
    val bookId: String?,
    val userId: String,
)
