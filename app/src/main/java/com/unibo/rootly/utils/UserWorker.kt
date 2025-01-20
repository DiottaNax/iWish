package com.unibo.rootly.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unibo.rootly.R
import com.unibo.rootly.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UserCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params),
    KoinComponent {

    private val userViewModel: UserViewModel by inject()

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
                        val inscriptionDate: LocalDate = userViewModel.user?.inscriptionDate ?: LocalDate.now()

                        when {
                            !achieved1 && isUsingAppForMonths(inscriptionDate, 1) -> {
                                achieved1 = true
                                userViewModel.insertBadge(
                                    applicationContext,
                                    applicationContext.getString(R.string.badge_1_month_name)
                                    ,userId)
                            }
                            !achieved2 && isUsingAppForMonths(inscriptionDate,6) -> {
                                achieved2 = true
                                userViewModel.insertBadge(
                                    applicationContext,
                                    applicationContext.getString(R.string.badge_6_months_name)
                                    ,userId)
                            }
                            !achieved3 && isUsingAppForMonths(inscriptionDate,12) -> {
                                achieved2 = true
                                userViewModel.insertBadge(
                                    applicationContext,
                                    applicationContext.getString(R.string.badge_1_year_name)
                                    ,userId)
                            }
                        }
                    }
                }
            }
            return Result.success()
        }

    private fun isUsingAppForMonths(inscriptionDate: LocalDate, min: Int): Boolean =
        ChronoUnit.MONTHS.between(inscriptionDate, LocalDate.now()) >= min
}
