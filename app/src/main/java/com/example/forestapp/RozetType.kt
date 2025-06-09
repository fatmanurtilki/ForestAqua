package com.example.forestapp

object RozetType {
    const val DENIZATI = "Deniz Atı Rozeti"
    const val YELKENLI = "Yelkenli Rozeti"
    const val DENIZKABUK = "Deniz Kabugu Rozeti"
    const val INCI = "Inci Rozeti"
    const val DENIZALTI = "Denizalti Rozeti"
    const val DALGA = "Dalga Rozeti"
    const val KAPLUMBAGA  = "Kaplumbaga Rozeti"
    const val DENIZYILDIZI = "Deniz Yildizi Rozeti"

    fun getAllRozetTypes() = listOf(
        DENIZATI, YELKENLI, DENIZKABUK, INCI,
        DENIZALTI, DALGA, KAPLUMBAGA, DENIZYILDIZI
    )

    val rozetDrawables = mapOf(
        DENIZATI to R.drawable.rozet_denizati,
        YELKENLI to R.drawable.rozet_gemi,
        DENIZKABUK to R.drawable.rozet_kabuk,
        INCI to R.drawable.rozet_inci,
        DENIZALTI to R.drawable.rozet_denizalti,
        DALGA to R.drawable.rozet_dalga,
        KAPLUMBAGA to R.drawable.rozet_kaplu,
        DENIZYILDIZI to R.drawable.rozet_yildiz
    )
}