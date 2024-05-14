package com.unibo.rootly.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Badge_Type")
data class BadgeType(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "img") val image: String,
    @ColumnInfo(name = "description") val description: String
)

@Entity(tableName = "Fertilizer", primaryKeys = [ "plant_id", "date"])
data class Fertilizer(
    @ColumnInfo(name = "plant_id") val plantId: Int,
    @ColumnInfo(name = "date") val date: LocalDate
)

@Entity(tableName = "Plant")
data class Plant(
    @ColumnInfo(name = "user_id") val userId: Int,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "plant_id") val plantId: Int = 0,
    @ColumnInfo(name = "plant_name") val plantName: String,
    @ColumnInfo(name = "dead") val isDead: Boolean = false,
    @ColumnInfo(name = "favorite") val isFavorite: Boolean= false,
    @ColumnInfo(name = "birthday") val birthday: LocalDate,
    @ColumnInfo(name = "scientific_name") val scientificName: String,
    @ColumnInfo(name = "img") val img: String? = null
)

@Entity(tableName = "Received", primaryKeys = ["name", "user_id"])
data class Received(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "user_id") val userId: Int
)

@Entity(tableName = "Specie")
data class Species(
    @PrimaryKey @ColumnInfo(name = "scientific_name") val scientificName: String,
    @ColumnInfo(name = "water_frequency") val waterFrequency: Int,
    @ColumnInfo(name = "light_level") val lightLevel: Int,
    @ColumnInfo(name = "fertilizer_frequency") val fertilizerFrequency: Int,
    @ColumnInfo(name = "tem_max") val maxTemperature: Float,
    @ColumnInfo(name = "tem_min") val minTemperature: Float
)
@Entity(tableName = "Users")
data class User(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val userId: Int = 0,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "location") val location: String? = null,
    @ColumnInfo(name = "profile_img") val profileImg: String? = null,
)

@Entity(tableName = "Water", primaryKeys = ["plant_id", "date"])
data class Water(
    @ColumnInfo(name = "plant_id") val plantId: Int,
    @ColumnInfo(name = "date") val date: LocalDate
)

