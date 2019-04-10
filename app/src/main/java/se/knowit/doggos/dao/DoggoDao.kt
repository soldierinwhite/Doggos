package se.knowit.doggos.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import se.knowit.doggos.data.entity.Breed
import se.knowit.doggos.data.entity.BreedImage

@Dao
interface DoggoDao {
    //Breed
    @Query("SELECT * FROM breed")
    fun getAllBreeds(): Single<List<Breed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBreeds(breeds: List<Breed>): Completable

    @Delete
    fun deleteBreeds(breeds: List<Breed>)

    @Query("UPDATE breedimage SET imageUrl = NULL")
    fun removeImageUrls(): Completable

    //BreedImage
    @Query("SELECT imageUrl FROM breedimage WHERE breedimage.name LIKE :name")
    fun getBreedImage(name: String): Single<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBreedImages(images: List<BreedImage>): Completable

    @Delete
    fun deleteBreedImages(breedImages: List<BreedImage>): Completable
}
