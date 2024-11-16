package com.maran.test.network

import kotlinx.serialization.Serializable

@Serializable
class Experience(
    val previewText: String,
    val text: String
)