package com.maran.test.network

import kotlinx.serialization.Serializable

@Serializable
class Offer(
    val id: String? = null,
    val title: String,
    val button: Button? = null,
    val link: String
)
