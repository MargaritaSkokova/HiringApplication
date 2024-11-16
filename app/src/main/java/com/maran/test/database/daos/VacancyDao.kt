package com.maran.test.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maran.test.database.entites.Vacancy
import java.util.UUID

@Dao
interface VacancyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vacancy: Vacancy)

    @Delete
    suspend fun delete(vacancy: Vacancy)

    @Query("SELECT * FROM vacancy")
    suspend fun getAll(): List<Vacancy>

    @Query("SELECT * FROM vacancy WHERE vacancy.id == :id")
    suspend fun findById(id: UUID): Vacancy
}