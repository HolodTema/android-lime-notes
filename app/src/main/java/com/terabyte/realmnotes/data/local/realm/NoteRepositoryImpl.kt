package com.terabyte.realmnotes.data.local.realm

import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.Note
import com.terabyte.realmnotes.domain.repository.NoteRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import org.mongodb.kbson.ObjectId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(private val realm: Realm): NoteRepository {

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
                    categoryId = note.categoryId
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
                findLatest(it)?.apply {
                    delete(this)
                }
            }
        }
    }

    override suspend fun getAllNotes(): List<Note> {
        val realmNoteObjList = realm.query<NoteRealmObject>().find().toList()
        return realmNoteObjList.map {
            it.toNote()
        }
    }

    override suspend fun deleteAllNotes() {
        realm.write {
            delete(NoteRealmObject::class)
        }
    }

    override suspend fun getAllCategories(): List<Category> {
        val categoryRealmObjList = realm.query<CategoryRealmObject>().find().toList()
        return categoryRealmObjList.map {
            it.toCategory()
        }
    }

    override suspend fun addCategory(category: Category) {
        realm.write {
            copyToRealm(CategoryRealmObject.fromCategory(category))
        }
    }

    override suspend fun updateCategory(category: Category) {
        if (category.id == null) {
            return
        }

        realm.write {
            val objId = ObjectId(category.id)
            val query = query(CategoryRealmObject::class, "id == $0", objId)
            val categoryRealmObj = query.first().find()
            ObjectId.invoke()

            categoryRealmObj?.let {
                findLatest(it)?.apply {
                    name = category.name
                    color = category.color
                }
            }
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        realm.write {
            val query = realm.query(CategoryRealmObject::class, "id == $0", ObjectId(categoryId))
            val categoryRealmObj = query.first().find()

            categoryRealmObj?.let {
                findLatest(it)?.apply {
                    delete(this)
                }
            }
        }
    }

    override suspend fun deleteAllCategories() {
        realm.write {
            delete(CategoryRealmObject::class)
        }
    }

    override suspend fun removeCategoryIdFromAllNotes(categoryId: String) {
        val notes = getAllNotes().filter { it.categoryId == categoryId }
        notes.forEach { note ->
            note.categoryId = null
            updateNote(note)
        }
    }

    override fun close() {
        realm.close()
    }
}