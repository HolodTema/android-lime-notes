package com.terabyte.realmnotes.data.local.realm

import com.terabyte.realmnotes.domain.model.Note
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class NoteRealmObject : RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId()
    var text: String = ""
    var date: Long = 0L

    fun toNote(): Note {
        return Note(
            id = id.toString(),
            text = text,
            date = Date(date)
        )
    }

    companion object {

        fun fromNote(note: Note): NoteRealmObject {
            val result = NoteRealmObject()
            result.apply {
                text = note.text
                date = note.date.time
            }

            if (note.id != null) {
                result.id = ObjectId(note.id)
            }

            return result
        }

    }
}
