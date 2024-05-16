package com.unibo.rootly.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unibo.rootly.viewmodel.PlantViewModel
import com.unibo.rootly.viewmodel.UserViewModel
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

    private val userViewModel: UserViewModel by inject()
    private val plantViewModel: PlantViewModel by inject()

        override fun doWork(): Result {
            Log.d("PlantCheckWorker", "doWork() started")
            val sharedPreferences: SharedPreferences =
                applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId",-1)

            if(userId > -1){
                Log.d("PlantCheckWorker", "userIDFound")
                runBlocking {
                    withContext(Dispatchers.IO) {
                        var achieved1 = false
                        var achieved2 = false
                        var achieved3 = false
                        Log.d("PlantCheckWorker", "in dispatcher")
                        plantViewModel.getPlantsByUser(userId).collect{ plants ->
                            Log.d("PlantCheckWorker", "list of ${plants.size} plants")
                            plants.forEach {plant ->
                                Log.d("PlantCheckWorker", "checking ${plant.plantId}")
                                if(!achieved1 && isPlantAliveForMonths(plant.birthday,1)){
                                    achieved1 = true
                                    Log.d("PlantCheckWorker", "adding 1 for plant ${plant.plantId}")
                                    plantViewModel.insertBadge(MONTH_BADGE ,userId)
                                }
                                if(!achieved2 && isPlantAliveForMonths(plant.birthday,6)){
                                    achieved2 = true
                                    Log.d("PlantCheckWorker", "adding 2 for plant ${plant.plantId}")
                                    plantViewModel.insertBadge(HALF_YEAR_BADGE,userId)
                                }
                                if(!achieved3 && isPlantAliveForMonths(plant.birthday,12)){
                                    achieved3 = true
                                    Log.d("PlantCheckWorker", "adding 3 for plant ${plant.plantId}")
                                    plantViewModel.insertBadge(YEAR_BADGE,userId)
                                }
                            }
                        }
                    }
                }
            }else{
                Log.d("PlantCheckWorker", "userId not Found")
            }
            Log.d("PlantCheckWorker", "doWork() completed")
            return Result.success()
        }

    private fun isPlantAliveForMonths(plantPlantedDate: LocalDate, min: Int): Boolean =
        ChronoUnit.MONTHS.between(plantPlantedDate, LocalDate.now()) >= min
}
