package com.unibo.petly.data.database

import android.content.Context
import com.unibo.petly.R
import java.time.LocalDate

object InitialData {

    fun getInitialUsers(): List<User> = listOf(
        User(username = "Astro", password = "petly", inscriptionDate =  LocalDate.now().minusMonths(13))
    )
    fun getInitialPetSpecies(): List<PetSpecies> {
        return listOf(
            PetSpecies("Dog", 1, DietType.OMNIVORE, 7, 35.0f, -10.0f),
            PetSpecies("Cat", 1, DietType.CARNIVORE, 10, 30.0f, 0.0f),
            PetSpecies("Hamster", 1, DietType.HERBIVORE, 5, 25.0f, 15.0f),
            PetSpecies("Rabbit", 1, DietType.HERBIVORE, 7, 28.0f, 5.0f),
            PetSpecies("Parrot", 1, DietType.OMNIVORE, 3, 30.0f, 18.0f),
            PetSpecies("Goldfish", 1, DietType.OMNIVORE, 10, 28.0f, 18.0f),
            PetSpecies("Turtle", 2, DietType.HERBIVORE, 15, 30.0f, 20.0f),
            PetSpecies("Snake", 10, DietType.CARNIVORE, 14, 35.0f, 20.0f)
        )
    }
    fun getBadgeTypes(context: Context): List<BadgeType> {
        return listOf(
            BadgeType(
                context.getString(R.string.badge_first_pet_name),
                R.drawable.badge_one_pet,
                context.getString(R.string.badge_first_pet_description)
            ),
            BadgeType(
                context.getString(R.string.badge_five_pets_name),
                R.drawable.badge_5_pet,
                context.getString(R.string.badge_five_pets_description)
            ),
            BadgeType(
                context.getString(R.string.badge_ten_pets_name),
                R.drawable.badge_10_pets,
                context.getString(R.string.badge_ten_pets_description)
            ),
            BadgeType(
                context.getString(R.string.badge_cleaning_guru_name),
                R.drawable.badge_cleaning,
                context.getString(R.string.badge_cleaning_guru_description)
            ),
            BadgeType(
                context.getString(R.string.badge_feeding_guru_name),
                R.drawable.badge_feeding,
                context.getString(R.string.badge_feeding_guru_description)
            ),
            BadgeType(
                context.getString(R.string.badge_first_cleaning_name),
                R.drawable.badge_cleaning,
                context.getString(R.string.badge_first_cleaning_description)
            ),
            BadgeType(
                context.getString(R.string.badge_first_feeding_name),
                R.drawable.badge_feeding,
                context.getString(R.string.badge_first_feeding_description)
            ),
            BadgeType(
                context.getString(R.string.badge_cleaning_bro_name),
                R.drawable.badge_cleaning,
                context.getString(R.string.badge_cleaning_bro_description)
            ),
            BadgeType(
                context.getString(R.string.badge_1_month_name),
                R.drawable.badge_one_month,
                context.getString(R.string.badge_1_month_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_6_months_name),
                R.drawable.badge_six_month,
                context.getString(R.string.badge_6_months_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_1_year_name),
                R.drawable.badge_one_year,
                context.getString(R.string.badge_1_year_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_feeding_bro_name),
                R.drawable.badge_feeding,
                context.getString(R.string.badge_feeding_bro_description)
            )
        )
    }
}

