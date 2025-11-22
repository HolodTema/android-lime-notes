package com.terabyte.realmnotes.domain.model

import java.io.Serializable

data class Category(
    val id: String? = null,
    var name: String = "",
    var color: Int = 0x000000
): Serializable, Comparable<Category> {

    override fun compareTo(other: Category): Int {
        return name.compareTo(other.name)
    }

}
