package com.terabyte.realmnotes.domain.model

import java.io.Serializable
import java.util.Date

data class Note(
    val id: String? = null,
    var text: String = "",
    val date: Date = Date()
): Serializable