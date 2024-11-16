package com.maran.test.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maran.test.database.VacancyRepository
import com.maran.test.network.Offer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.UUID


class MainViewModel(
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) : ViewModel() {
    var isVacancyAllShown: Boolean = false
    private val db: VacancyRepository = VacancyRepository.get()

    suspend fun getOffers(file: InputStream): List<Offer> {
        val offers = coroutineScope.async {
            parseOffers(file)
        }

        return offers.await()
    }

    fun parseOffers(file: InputStream): List<Offer> {
        val json = file.bufferedReader().use { it.readText() }
        val offers = Gson().fromJson<List<Offer>>(json, object : TypeToken<List<Offer>>() {}.type)

        return offers
    }

    suspend fun getVacancies(file: InputStream): List<com.maran.test.network.Vacancy> {
        val vacancies = coroutineScope.async {
            parseVacancies(file)
        }

        return vacancies.await()
    }

    fun parseVacancies(file: InputStream): List<com.maran.test.network.Vacancy> {
        val json = file.bufferedReader().use { it.readText() }
        val vacancies = Gson().fromJson<List<com.maran.test.network.Vacancy>>(
            json,
            object : TypeToken<List<com.maran.test.network.Vacancy>>() {}.type
        )

        return vacancies
    }

    fun addFavourite(vacancy: com.maran.test.database.entites.Vacancy) {
        coroutineScope.launch { db.addFavourite(vacancy) }
    }

    fun deleteFavourite(id: UUID) {
        coroutineScope.launch { db.deleteFavourite(id) }
    }

    suspend fun isFavourite(id: UUID): Boolean {
        return coroutineScope.async { db.isFavourite(id) }.await()
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}