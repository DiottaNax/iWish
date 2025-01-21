package com.unibo.petly.data.database

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Badge_Type")
data class BadgeType(
    @PrimaryKey val name: String,
    @DrawableRes @ColumnInfo(name = "img") val imageResourceId: Int,
    @ColumnInfo(name = "description") val description: String
)


@Entity(tableName = "Cleaning", primaryKeys = [ "pet_id", "date"])
data class Cleaning(
    @ColumnInfo(name = "pet_id") val petId: Int,
    @ColumnInfo(name = "date") val date: LocalDate
)

@Entity(tableName = "Pet")
data class Pet(
    @ColumnInfo(name = "user_id") val userId: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "pet_id") val petId: Int = 0,
    @ColumnInfo(name = "pet_name") val petName: String,
    @ColumnInfo(name = "removed") val isRemoved: Boolean = false,
    @ColumnInfo(name = "favorite") val isFavorite: Boolean= false,
    @ColumnInfo(name = "birthday") val birthday: LocalDate,
    @ColumnInfo(name = "specie") val specie: String,
    @ColumnInfo(name = "img") val img: String? = null
)

@Entity(tableName = "Received", primaryKeys = ["name", "user_id"])
data class Received(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "user_id") val userId: Int
)

enum class DietType {
    CARNIVORE,
    HERBIVORE,
    OMNIVORE
}

@Entity(tableName = "Pet_Species")
data class PetSpecies(
    @PrimaryKey @ColumnInfo(name = "specie_name") val specieName: String,
    @ColumnInfo(name = "feeding_frequency") val feedingFrequency: Int,
    @ColumnInfo(name = "diet_type") val dietType: DietType,
    @ColumnInfo(name = "cleaning_frequency") val cleaningFrequency: Int,
    @ColumnInfo(name = "tem_max") val maxTemperature: Float,
    @ColumnInfo(name = "tem_min") val minTemperature: Float
)
@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val userId: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "inscription_date") val inscriptionDate: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "location") val location: String? = null,
    @ColumnInfo(name = "profile_img") val profileImg: String? = null,
)

@Entity(tableName = "Feeding", primaryKeys = ["pet_id", "date"])
data class Feeding(
    @ColumnInfo(name = "pet_id") val petId: Int,
    @ColumnInfo(name = "date") val date: LocalDate
)

