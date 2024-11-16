package com.maran.test.database.entites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maran.test.network.Vacancy
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity(tableName = "vacancy")
class Vacancy(
    @PrimaryKey
    @Contextual
    val id: UUID,
    @ColumnInfo(name = "looking_number")
    val lookingNumber: Int,
    val title: String,
    val town: String,
    val street: String,
    val house: String,
    val company: String,
    @ColumnInfo(name = "experience_preview_text")
    val experiencePreviewText: String,
    @ColumnInfo(name = "experience_text")
    val experienceText: String,
    @ColumnInfo(name = "published_date")
    val publishedDate: String,
    @ColumnInfo(name = "is_favourite")
    val isFavourite: Boolean,
    @ColumnInfo(name = "salary_short")
    val salaryShort: String?,
    @ColumnInfo(name = "salary_full")
    val salaryFull: String?,
    val schedules: List<String>,
    @ColumnInfo(name = "applied_number")
    val appliedNumber: Int?,
    val description: String?,
    val responsibilities: String,
    val questions: List<String>
) {
    companion object {
        fun fromNetworkVacancy(vacancy: Vacancy): com.maran.test.database.entites.Vacancy {
            return Vacancy(
                id = vacancy.id,
                lookingNumber = vacancy.lookingNumber,
                title = vacancy.title,
                town = vacancy.address.town,
                street = vacancy.address.street,
                house = vacancy.address.house,
                company = vacancy.company,
                experiencePreviewText = vacancy.experience.previewText,
                experienceText = vacancy.experience.text,
                publishedDate = vacancy.publishedDate,
                isFavourite = vacancy.isFavourite,
                salaryShort = vacancy.salary.short,
                salaryFull = vacancy.salary.full,
                schedules = vacancy.schedules,
                appliedNumber = vacancy.appliedNumber,
                description = vacancy.description,
                responsibilities = vacancy.responsibilities,
                questions = vacancy.questions
                )
        }

        fun fromVacancySerializable(vacancy: VacancySerializable): com.maran.test.database.entites.Vacancy{
            return Vacancy(
                id = vacancy.id,
                lookingNumber = vacancy.lookingNumber,
                title = vacancy.title,
                town = vacancy.town,
                street = vacancy.street,
                house = vacancy.house,
                company = vacancy.company,
                experiencePreviewText = vacancy.experiencePreviewText,
                experienceText = vacancy.experienceText,
                publishedDate = vacancy.publishedDate,
                isFavourite = vacancy.isFavourite,
                salaryShort = vacancy.salaryShort,
                salaryFull = vacancy.salaryFull,
                schedules = vacancy.schedules,
                appliedNumber = vacancy.appliedNumber,
                description = vacancy.description,
                responsibilities = vacancy.responsibilities,
                questions = vacancy.questions
            )
        }
    }
}