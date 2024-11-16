package com.maran.test.viewModels

import androidx.lifecycle.ViewModel
import com.maran.test.database.VacancyRepository
import com.maran.test.database.entites.Vacancy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.UUID

class PageVacancyViewModel
    (
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate),
    private val db: VacancyRepository = VacancyRepository.get()
) : ViewModel() {
    suspend fun getFavourite(): List<Vacancy> {
        return coroutineScope.async { db.getAll() }.await()
    }

    fun addFavourite(vacancy: Vacancy) {
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