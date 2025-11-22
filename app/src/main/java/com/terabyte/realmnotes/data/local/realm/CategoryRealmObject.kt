package com.terabyte.realmnotes.data.local.realm

import com.terabyte.realmnotes.domain.model.Category
import com.terabyte.realmnotes.domain.model.Note
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class CategoryRealmObject : RealmObject {
    @PrimaryKey var id: ObjectId = ObjectId()
    var name: String = ""
    var color: Int = 0x626262

    fun toCategory(): Category {
        return Category(
            id = id.toHexString(),
            name = name,
            color = color
        )
    }

    companion object {

        fun fromCategory(category: Category): CategoryRealmObject {
            val result = CategoryRealmObject()
            result.apply {
                name = category.name
                color = category.color
            }

            if (category.id != null) {
                result.id = ObjectId(category.id)
            }

            return result
        }

    }
}
