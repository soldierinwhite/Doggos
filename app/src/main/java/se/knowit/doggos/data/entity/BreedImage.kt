package se.knowit.doggos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BreedImage (
    @PrimaryKey val name: String,
    val imageUrl: String?
)
