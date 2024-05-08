package com.unibo.rootly.data.repositories

import androidx.annotation.WorkerThread
import com.unibo.rootly.data.database.Species
import com.unibo.rootly.data.database.daos.SpeciesDao
import javax.inject.Inject

class SpeciesRepository @Inject constructor(
    private val speciesDao: SpeciesDao
) {
    @WorkerThread
    suspend fun insert(species: Species) = speciesDao.insertSpecies(species)
    @WorkerThread
    suspend fun getByName(name: String) = speciesDao.getSpeciesByName(name)

}