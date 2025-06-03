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

    fun getAllRozetTypes(): List<String> = listOf(DENIZATI, YELKENLI, DENIZKABUK, INCI, DENIZALTI, DALGA, KAPLUMBAGA, DENIZYILDIZI)

    // Rozetler ve kazanma koşulları sallamasyon (dikilen ağaç sayısı)
    val rozetRequirements = mapOf(
        DENIZATI to 10,
        YELKENLI to 20,
        DENIZKABUK to 25,
        INCI to 0, // özel durum, aşağıda kontrol edilecek
        DENIZALTI to 2,  // 2 gün 10dk üstü
        DALGA to 7,      // 7 gün 25dk üstü
        KAPLUMBAGA to 100,
        DENIZYILDIZI to 60
    )


    // Rozet drawable ID'leri
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

    fun getEarnedRozets(treesPlanted: Int): List<String> {
        return rozetRequirements.filter { treesPlanted >= it.value }.keys.toList()
    }
}