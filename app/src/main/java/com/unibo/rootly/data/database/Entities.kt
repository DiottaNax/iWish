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

@Entity(tableName = "Fertilizer", primaryKeys = ["user_id", "plant_id", "date"])
data class Fertilizer(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "plant_id") val plantId: Int,
    @ColumnInfo(name = "date") val date: LocalDate
)

@Entity(tableName = "Likes", primaryKeys = ["user_id", "plant_id"])
data class Likes(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "plant_id") val plantId: Int
)

@Entity(tableName = "Plant", primaryKeys = ["user_id", "plant_id"])
data class Plant(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "plant_id") val plantId: Int,
    @ColumnInfo(name = "plant_name") val plantName: String,
    @ColumnInfo(name = "dead") val isDead: Char,
    @ColumnInfo(name = "birthday") val birthday: LocalDate,
    @ColumnInfo(name = "scientific_name") val scientificName: String
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

@Entity(tableName = "Plant_log", primaryKeys = ["user_id", "plant_id", "date"])
data class PlantLog(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "plant_id") val plantId: Int,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture") val picture: String,
    @ColumnInfo(name = "height") val height: Float
)

@Entity(tableName = "Users")
data class User(
    @PrimaryKey @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "location") val location: String,
    @ColumnInfo(name = "profile_img") val profileImg: String,
    @ColumnInfo(name = "n_plants") val numberOfPlants: Int
)

@Entity(tableName = "Water", primaryKeys = ["user_id", "plant_id", "date"])
data class Water(
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "plant_id") val plantId: Int,
    @ColumnInfo(name = "date") val date: LocalDate
)

