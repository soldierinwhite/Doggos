package se.knowit.doggos.data.response

data class BreedImagesResponse (
    val status: String?,
    val message: List<String>
)  {
    companion object {
        const val STATUS_SUCCESS = "success"
    }
}
