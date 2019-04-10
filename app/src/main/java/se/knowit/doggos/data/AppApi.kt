package se.knowit.doggos.data

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import se.knowit.doggos.data.response.BreedImagesResponse
import se.knowit.doggos.data.response.BreedsResponse

interface AppApi {
    @GET ("api/breeds/list/all")
    fun getBreedsList(): Observable<BreedsResponse>

    @GET("api/breed/{name}/images/random/1")
    fun getBreedPicture(
        @Path("name") name: String
    ): Observable<BreedImagesResponse>
}
