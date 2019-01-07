package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Tag

@Dao
interface TagDao {

    @Query("SELECT * FROM tag ORDER BY tagId DESC")
    fun getAllTags(): Flowable<List<Tag>>

    @Query("UPDATE tag SET name = :tagName WHERE tagId = :tagId")
    fun updateTag(tagId: Int, tagName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(tag: Tag)

    @Delete
    fun deleteTag(tag: Tag)
}