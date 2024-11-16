package com.maran.test.database

import android.content.Context
import androidx.room.Room
import java.util.UUID

class VacancyRepository private constructor(context: Context) {
    val db = Room.databaseBuilder(
        context,
        VacancyDatabase::class.java, "database-vacancy"
    ).build()

    private val vacancyDao = db.vacancyDao()

    companion object {
        private var instance: VacancyRepository? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = VacancyRepository(context)
            }
        }

        fun get(): VacancyRepository {
            return instance ?: throw IllegalArgumentException()
        }
    }

    suspend fun addFavourite(vacancy: com.maran.test.database.entites.Vacancy) {
        vacancyDao.insert(vacancy)
    }

    suspend fun deleteFavourite(id: UUID) {
        val vacancy = findById(id)
        if (vacancy != null)
            vacancyDao.delete(vacancy)
    }

    suspend fun findById(id: UUID): com.maran.test.database.entites.Vacancy? {
        return vacancyDao.findById(id)
    }

    suspend fun isFavourite(id: UUID): Boolean {
        return findById(id) != null
    }

    suspend fun getAll(): List<com.maran.test.database.entites.Vacancy> {
        return vacancyDao.getAll()
    }
}