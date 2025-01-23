package com.unibo.petly.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.unibo.petly.R
import com.unibo.petly.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UserCheckWorker(context: Context, params: WorkerParameters) : Worker(context, params),
    KoinComponent {

        override fun doWork(): Result {
            val sharedPreferences: SharedPreferences =
                applicationContext.getSharedPreferences("userId", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getInt("userId",-1)
            var achieved1 = false
            var achieved2 = false
            var achieved3 = false

            println("GREAT DAY TODAY")
            val userViewModel = UserViewModel()
            userViewModel.setUser(userId)
            println("Doing Work for user: " + userViewModel.user?.username );

            if(userId > -1){
                runBlocking {
                    withContext(Dispatchers.IO) {
                        val inscriptionDate: LocalDate = userViewModel.user?.inscriptionDate ?: LocalDate.now()

                        if(!achieved1 && isUsingAppForMonths(inscriptionDate, 1)) {
                                achieved1 = true
                                userViewModel.insertBadge(
                                    applicationContext,
                                    applicationContext.getString(R.string.badge_1_month_name)
                                    ,userId)
                        }
                        if (!achieved2 && isUsingAppForMonths(inscriptionDate,6)) {
                                achieved2 = true
                                userViewModel.insertBadge(
                                    applicationContext,
                                    applicationContext.getString(R.string.badge_6_months_name)
                                    ,userId)
                        }
                        if (!achieved3 && isUsingAppForMonths(inscriptionDate,12)) {
                                achieved3 = true
                                userViewModel.insertBadge(
                                    applicationContext,
                                    applicationContext.getString(R.string.badge_1_year_name)
                                    ,userId)
                        }
                    }
                }
            }
            return Result.success()
        }

    private fun isUsingAppForMonths(inscriptionDate: LocalDate, min: Int): Boolean =
        ChronoUnit.MONTHS.between(inscriptionDate, LocalDate.now()) >= min
}
