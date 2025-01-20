package com.unibo.rootly.data.database

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.unibo.rootly.R

object InitialData {
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
}

    fun getBadgeTypes(context: Context): List<BadgeType> {
        return listOf(
            BadgeType(
                context.getString(R.string.badge_1_plant_name),
                R.drawable.badge1plant,
                context.getString(R.string.badge_1_plant_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_10_plants_name),
                R.drawable.badge10plant,
                context.getString(R.string.badge_10_plants_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_50_plants_name),
                R.drawable.badge50plant,
                context.getString(R.string.badge_50_plants_name)
            ),
            BadgeType(
                context.getString(R.string.badge_100_plants_name),
                R.drawable.badge100plant,
                context.getString(R.string.badge_100_plants_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_1_month_name),
                R.drawable.badge1month,
                context.getString(R.string.badge_1_month_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_6_months_name),
                R.drawable.badge6month,
                context.getString(R.string.badge_6_months_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_1_year_name),
                R.drawable.badge1year,
                context.getString(R.string.badge_1_year_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_water_name),
                R.drawable.badge_water,
                context.getString(R.string.badge_water_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_fertilizer_name),
                R.drawable.badge_fertilize,
                context.getString(R.string.badge_fertilizer_descr)
            ),
            BadgeType(
                context.getString(R.string.badge_death_name),
                R.drawable.badge_dead,
                context.getString(R.string.badge_death_descr)
            )

        )
    }
}