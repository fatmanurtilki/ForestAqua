package com.example.forestapp

object TreeType {
    const val ASTRONOT = "Astronot"
    const val PALYACO = "Palayaco"
    const val DISKUS = "Diskus"
    const val JAPON = "Japon"
    const val MOLI = "Moli"
    const val MELEK = "Melek"
    const val BETA  = "Beta"
    const val PAPAGAN = "Papagan"

    fun getAllTypes(): List<String> = listOf(ASTRONOT, PALYACO, DISKUS, JAPON, MOLI, MELEK, BETA, PAPAGAN)

    fun getCoinValue(type: String): Int = when (type) {
        ASTRONOT -> 10
        PALYACO -> 20
        DISKUS -> 15
        JAPON -> 25
        MOLI -> 30
        MELEK -> 20
        BETA -> 30
        PAPAGAN -> 20
        else -> 10
    }
}