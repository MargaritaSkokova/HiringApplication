package com.maran.test.network

import com.maran.test.database.entites.Vacancy
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Vacancy(
    @Contextual
    val id: UUID,
    val lookingNumber: Int,
    val title: String,
    val address: Address,
    val company: String,
    val experience: Experience,
    val publishedDate: String,
    val isFavourite: Boolean,
    val salary: Salary,
    val schedules: List<String>,
    val appliedNumber: Int?,
    val description: String?,
    val responsibilities: String,
    val questions: List<String>
) {
}