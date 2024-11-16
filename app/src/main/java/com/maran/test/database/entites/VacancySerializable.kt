package com.maran.test.database.entites

import com.maran.test.network.Vacancy
import java.util.UUID

class VacancySerializable(
    val id: UUID,
    val lookingNumber: Int,
    val title: String,
    val town: String,
    val street: String,
    val house: String,
    val company: String,
    val experiencePreviewText: String,
    val experienceText: String,
    val publishedDate: String,
    val isFavourite: Boolean,
    val salaryShort: String?,
    val salaryFull: String?,
    val schedules: List<String>,
    val appliedNumber: Int?,
    val description: String?,
    val responsibilities: String,
    val questions: List<String>
) : java.io.Serializable {
    companion object {
        fun fromVacancyNetwork(vacancy: Vacancy): VacancySerializable {
            return VacancySerializable(
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

        fun fromVacancyEntity(vacancy: com.maran.test.database.entites.Vacancy): VacancySerializable {
            return VacancySerializable(
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