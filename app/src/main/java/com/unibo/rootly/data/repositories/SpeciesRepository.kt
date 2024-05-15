package com.unibo.rootly.data.repositories

import com.unibo.rootly.data.database.Species
import com.unibo.rootly.data.database.daos.SpeciesDao

class SpeciesRepository(
    private val speciesDao: SpeciesDao
) {
    suspend fun insert(species: Species) = speciesDao.insertSpecies(species)

    fun getByName(name: String) = speciesDao.getSpeciesByName(name)

    fun getAllSpeciesName() = speciesDao.getAllSpeciesName()
}