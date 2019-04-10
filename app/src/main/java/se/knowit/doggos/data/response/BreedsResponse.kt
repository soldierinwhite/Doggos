package se.knowit.doggos.data.response

import com.google.gson.JsonObject

data class BreedsResponse (
    val status: String?,
    val message: HashMap<String, List<String>?>
) {
    companion object {
        const val STATUS_SUCCESS = "success"
    }
}
