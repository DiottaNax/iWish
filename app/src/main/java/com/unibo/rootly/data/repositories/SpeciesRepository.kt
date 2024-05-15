package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Species
import com.unibo.rootly.data.database.daos.SpeciesDao

class SpeciesRepository(
    private val speciesDao: SpeciesDao
) {
    @WorkerThread
    suspend fun insert(species: Species) = speciesDao.insertSpecies(species)
    @WorkerThread
    fun getByName(name: String) = speciesDao.getSpeciesByName(name)

    @WorkerThread
    fun getAllSpeciesName() = speciesDao.getAllSpeciesName()
}