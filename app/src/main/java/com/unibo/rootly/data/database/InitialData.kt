package com.unibo.rootly.data.database

import java.time.LocalDate

object InitialData {
    fun getInitialSpecies(): List<Species> {
        return listOf(
            Species("Spider Plant", 15, 2, 65, 25.0f, 10.0f),
            Species("Snake Plant", 12, 1, 50, 30.0f, 15.0f),
            Species("Pothos", 18, 1, 75, 25.0f, 15.0f),
            Species("Peace Lily", 14, 1, 60, 25.0f, 15.0f),
            Species("ZZ Plant", 11, 1, 35, 30.0f, 15.0f),
            Species("Monstera", 16, 2, 80, 30.0f, 15.0f),
            Species("Philodendron", 19, 2, 70, 25.0f, 15.0f),
            Species("Rubber Plant", 13, 2, 45, 25.0f, 10.0f),
            Species("Fiddle Leaf Fig", 17, 3, 85, 25.0f, 15.0f),
            Species("Aloe Vera", 20, 2, 90, 35.0f, 10.0f),
            Species("English Ivy", 15, 1, 60, 25.0f, 15.0f),
            Species("Chinese Evergreen", 19, 1, 70, 25.0f, 15.0f),
            Species("Parlor Palm", 11, 1, 45, 25.0f, 15.0f),
            Species("Fern", 18, 2, 75, 25.0f, 15.0f),
            Species("Jade Plant", 20, 2, 85, 35.0f, 10.0f),
            Species("Succulent", 13, 2, 90, 35.0f, 10.0f),
            Species("Calathea", 15, 2, 65, 25.0f, 15.0f),
            Species("Bird of Paradise", 16, 3, 75, 30.0f, 15.0f),
            Species("Money Tree", 19, 2, 90, 30.0f, 15.0f),
            Species("Dracaena", 18, 2, 70, 30.0f, 15.0f),
            Species("Pilea", 20, 1, 75, 25.0f, 15.0f),
            Species("Christmas Cactus", 14, 1, 35, 25.0f, 10.0f),
            Species("Hoya", 12, 1, 75, 25.0f, 15.0f),
            Species("Dieffenbachia", 17, 1, 60, 25.0f, 15.0f),
            Species("Oxalis", 18, 1, 65, 25.0f, 15.0f),
            Species("Bromeliad", 15, 2, 40, 25.0f, 15.0f),
            Species("Schefflera", 16, 1, 70, 25.0f, 15.0f),
            Species("String of Pearls", 20, 1, 90, 35.0f, 10.0f)
        )
    }

    fun getBadgeTypes(): List<BadgeType> {
        //todo other badges and imgs
        return listOf(
            BadgeType("First Timer", "",
                "you added your first plant to rootly !"),
            BadgeType("Sprout Scout", "",
                "yuo have 10 or more alive plants"),
            BadgeType("Green Thumb", "",
            "yuo have 50 or more alive plants"),
            BadgeType("Botanical Master", "badge2_image",
                "yuo have 100 or more alive plants"),
            BadgeType("Budding Caretaker","",
                "one of your plants lived for a month"),
            BadgeType("Thriving Guardian","",
                "one of your plants lived for 6 months"),
            BadgeType("Perennial Protector","",
                "one of your plants lived for a year"),
            BadgeType("Water Warrior","",
                "you watered plants 100 times or more"),
            BadgeType("Zen Gardener","",
                "you fertilized plants 100 times or more"),
            BadgeType("Heartfelt Mourner","",
                "you lost a plant, but don't worry it happens to everyone")
        )
    }

    //user example with plants for test reasons

    fun getInitialUser(): User {
        return User( username = "ExampleUser", password = "password")
    }

    fun getInitialPlants(): List<Plant> {
        val plants = mutableListOf<Plant>()
        for (i in 1..10) {
            val species = getInitialSpecies()[i]
            plants.add(
                Plant(
                    userId = 1,
                    plantName = "Plant $i",
                    isDead = false,
                    isFavorite = false,
                    birthday = LocalDate.of(2022, 1, 1),
                    scientificName = species.scientificName,
                    img = null
                )
            )
        }
        return plants
    }

    fun getInitialWaters(): List<Water> {
        val waters = mutableListOf<Water>()
        for (i in 1..10) {
            waters.add(Water(plantId = i, date = LocalDate.of(2024, 4, i + 5)))
        }
        return waters
    }

    fun getInitialFertilizers(): List<Fertilizer> {
        val fertilizers = mutableListOf<Fertilizer>()
        for (i in 1..10) {
            fertilizers.add(Fertilizer(plantId = i, date = LocalDate.of(2024, 3, i + 5)))
        }
        return fertilizers
    }
}