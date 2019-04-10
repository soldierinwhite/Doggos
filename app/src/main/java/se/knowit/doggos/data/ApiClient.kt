package se.knowit.doggos.data

import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import se.knowit.doggos.data.entity.Breed
import se.knowit.doggos.data.response.BreedsResponse.Companion.STATUS_SUCCESS
import java.util.concurrent.TimeUnit

class ApiClient {
    private val BASE_URL = "https://dog.ceo/"
    private val appApi: AppApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .build())
            .build()

        appApi = retrofit.create(AppApi::class.java)
    }

    fun getBreeds(): Observable<List<Breed>> {
        return appApi.getBreedsList()
            .flatMap {
                if (it.status == STATUS_SUCCESS) {
                    Observable.just(
                        it.message.map { entry ->
                            Breed(entry.key, entry.value ?: listOf())
                        }.sortedBy { breed -> breed.name }
                    )
                } else {
                    Observable.error(Exception())
                }
            }
    }

    fun getImageForBreed(name: String): Observable<String> {
        return appApi.getBreedPicture(name)
            .flatMap {
                if (it.status == STATUS_SUCCESS) {
                    Observable.just(it.message[0])
                } else {
                    Observable.error(Exception())
                }
            }
    }

    companion object {
        val instance: ApiClient
                by lazy { ApiClient() }
    }
}
