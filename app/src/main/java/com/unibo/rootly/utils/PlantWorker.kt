package com.unibo.rootly.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unibo.rootly.viewmodel.PlantViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val MONTH_BADGE = "Budding Caretaker"
const val HALF_YEAR_BADGE = "Thriving Guardian"
const val YEAR_BADGE ="Perennial Protector"
class PlantCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params),
    KoinComponent {

    private val plantViewModel: PlantViewModel by inject()

        override fun doWork(): Result {
            val sharedPreferences: SharedPreferences =
                applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId",-1)

            if(userId > -1){
                runBlocking {
                    withContext(Dispatchers.IO) {
                        var achieved1 = false
                        var achieved2 = false
                        var achieved3 = false
                        plantViewModel.getPlantsByUser(userId).collect{ plants ->
                            plants.forEach {plant ->
                                if(!achieved1 && isPlantAliveForMonths(plant.birthday,1)){
                                    achieved1 = true
                                    plantViewModel.insertBadge(MONTH_BADGE ,userId)
                                }
                                if(!achieved2 && isPlantAliveForMonths(plant.birthday,6)){
                                    achieved2 = true
                                    plantViewModel.insertBadge(HALF_YEAR_BADGE,userId)
                                }
                                if(!achieved3 && isPlantAliveForMonths(plant.birthday,12)){
                                    achieved3 = true
                                    plantViewModel.insertBadge(YEAR_BADGE,userId)
                                }
                            }
                        }
                    }
                }
            }
            return Result.success()
        }

    private fun isPlantAliveForMonths(plantPlantedDate: LocalDate, min: Int): Boolean =
        ChronoUnit.MONTHS.between(plantPlantedDate, LocalDate.now()) >= min
}
