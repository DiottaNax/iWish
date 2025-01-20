package com.unibo.rootly.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibo.rootly.data.database.PetSpecies
import kotlinx.coroutines.flow.Flow

@Dao
interface PetSpeciesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpecies(species: PetSpecies)

    @Query("SELECT * FROM Pet_Species WHERE specie_name = :specieName")
    fun getSpeciesByName(specieName: String): PetSpecies?
    suspend fun insertAll(species: List<PetSpecies>) {
        species.forEach{ s -> insertSpecies(s)}
    }

    @Query("SELECT specie_name FROM Pet_Species")
    fun getAllSpeciesName() : Flow<List<String>>

}