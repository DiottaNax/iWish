package com.unibo.petly.data.repositories

import com.unibo.petly.data.database.PetSpecies
import com.unibo.petly.data.database.daos.PetSpeciesDao

class SpeciesRepository(
    private val speciesDao: PetSpeciesDao
) {
    suspend fun insert(species: PetSpecies) = speciesDao.insertSpecies(species)

    fun getByName(name: String) = speciesDao.getSpeciesByName(name)

    fun getAllSpeciesName() = speciesDao.getAllSpeciesName()
}