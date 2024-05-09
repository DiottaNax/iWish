package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.Species

@Dao
interface SpeciesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpecies(species: Species)

    @Query("SELECT * FROM Specie WHERE scientific_name = :scientificName")
    suspend fun getSpeciesByName(scientificName: String): Species?
    suspend fun insertAll(species: List<Species>) {
        species.forEach{ s -> insertSpecies(s)}
    }

}