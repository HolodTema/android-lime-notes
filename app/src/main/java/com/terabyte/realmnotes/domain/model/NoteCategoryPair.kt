package com.terabyte.realmnotes.domain.model

data class NoteCategoryPair(
    val note: Note,
    val category: Category?
): Comparable<NoteCategoryPair> {

    override fun compareTo(other: NoteCategoryPair): Int {
        return note.compareTo(other.note)
    }

}
