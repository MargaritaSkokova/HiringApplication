package com.maran.test.network

import kotlinx.serialization.Serializable

@Serializable
class Address (
    val town: String,
    val street: String,
    val house: String
)