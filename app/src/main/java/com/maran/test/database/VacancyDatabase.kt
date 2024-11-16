package com.maran.test.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maran.test.database.daos.VacancyDao
import com.maran.test.database.entites.Vacancy

@Database(entities = [Vacancy::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class VacancyDatabase: RoomDatabase()  {
    abstract fun vacancyDao(): VacancyDao
}