package se.knowit.doggos.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import se.knowit.doggos.data.ApiClient
import se.knowit.doggos.data.DoggoDatabase
import se.knowit.doggos.data.entity.Breed
import se.knowit.doggos.data.entity.BreedImage
import se.knowit.doggos.tools.fromIoToUi

class MainViewModel : ViewModel() {
    private val disposables = CompositeDisposable()
    private val apiClient = ApiClient()
    private val db = DoggoDatabase.getDb()

    val breeds: ObservableField<List<Breed>> = ObservableField(listOf())
    val breedErrorState = ObservableBoolean(false)
    val showProgressBar = ObservableBoolean(false)

    fun fetchBreeds() {
        showProgressBar.set(true)
        db?.let {
            disposables += it.doggoDao().getAllBreeds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ breeds ->
                    if (breeds.isNotEmpty()) {
                        setBreeds(breeds)
                    } else {
                        fetchBreedsFromApi()
                    }
                }, {
                    fetchBreedsFromApi()
                })
        }
    }

    fun refresh() {
        db?.let {
            disposables += it.doggoDao().removeImageUrls()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    fetchBreedsFromApi()
                }
        }
    }

    private fun fetchBreedsFromApi() {
        showProgressBar.set(true)
        disposables += apiClient.getBreeds()
            .fromIoToUi()
            .subscribe(this::setBreeds) {
                breedErrorState.set(true)
                showProgressBar.set(false)
            }
    }

    fun fetchImageForBreed(name: String, listener: (String?) -> Unit) {
        db?.let {
            disposables += it.doggoDao().getBreedImage(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ url ->
                    if (!url.isNullOrEmpty()) {
                        listener(url)
                    } else {
                        fetchImageFromApi(name, listener)
                    }
                }, {
                    fetchImageFromApi(name, listener)
                })
        }
    }

    private fun fetchImageFromApi(name: String, listener: (String?) -> Unit) {
        disposables += apiClient.getImageForBreed(name)
            .fromIoToUi()
            .subscribe({ url ->
                db?.let {
                    disposables += it.doggoDao().insertBreedImages(listOf(BreedImage(name, url)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Log.d("DetailViewModel", "Breed images inserted")
                        }
                }
                listener(url)
            }, {
                listener(null)
            })
    }

    private fun setBreeds(newBreeds: List<Breed>) {
        db?.let {
            disposables += it.doggoDao().insertBreeds(newBreeds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("MainViewModel", "Insert breeds success")
                }
        }
        breeds.set(newBreeds)
        showProgressBar.set(false)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
