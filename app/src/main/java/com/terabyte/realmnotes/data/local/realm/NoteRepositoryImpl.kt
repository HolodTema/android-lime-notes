package com.terabyte.realmnotes.data.local.realm

import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.domain.repository.NoteRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class NoteRepositoryImpl: NoteRepository {
    private val realmConfig = RealmConfiguration.Builder(
        schema = setOf(NoteRealmObject::class)
    )
        .deleteRealmIfMigrationNeeded() //for development
        .build()

    private val realm = Realm.open(realmConfig)

    override suspend fun updateNote(note: Note) {
        if (note.id == null) {
            return
        }

        realm.write {
            val objId = ObjectId(note.id)
            val query = query(NoteRealmObject::class, "id == $0", objId)
            val noteRealmObj = query.first().find()
            ObjectId.invoke()

            noteRealmObj?.let {
                findLatest(it)?.apply {
                    text = note.text
                }
            }
        }
    }

    override suspend fun addNote(note: Note) {
        realm.write {
            copyToRealm(NoteRealmObject.fromNote(note))
        }
    }

    override suspend fun deleteNote(noteId: String) {
        realm.write {
            val query = query(NoteRealmObject::class, "id == $0", ObjectId(noteId))
            val noteRealmObj = query.first().find()

            noteRealmObj?.let {
                delete(it)
            }
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        val realmNoteObjList = realm.query<NoteRealmObject>().find().toList()
        return realmNoteObjList.map {
            it.toNote()
        }
    }

    override fun close() {
        realm.close()
    }
}