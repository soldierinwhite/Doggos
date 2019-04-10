package se.knowit.doggos.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import se.knowit.doggos.DoggosApp
import se.knowit.doggos.dao.DoggoDao
import se.knowit.doggos.data.entity.Breed
import se.knowit.doggos.data.entity.BreedImage
import se.knowit.doggos.tools.Converter

@Database(entities = [Breed::class, BreedImage::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class DoggoDatabase : RoomDatabase() {
    abstract fun doggoDao(): DoggoDao

    companion object {
        @Volatile
        private var instance: DoggoDatabase? = null

        fun getDb(): DoggoDatabase? {
            DoggosApp.getInstance()?.applicationContext?.let {
                val tempInstance = instance
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    val newInstance = Room.databaseBuilder(it, DoggoDatabase::class.java, "qapital_db").build()
                    instance = newInstance
                    return newInstance
                }
            } ?: return null
        }
    }
}
