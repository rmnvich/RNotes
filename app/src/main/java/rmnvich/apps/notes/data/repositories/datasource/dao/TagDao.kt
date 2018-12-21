package rmnvich.apps.notes.data.repositories.datasource.dao

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Tag

@Dao
interface TagDao {

    @Query("SELECT * FROM tag ORDER BY tagId DESC")
    fun getAllTags(): Flowable<List<Tag>>

    @Query("SELECT * FROM tag WHERE tagId = :tagId")
    fun getTagById(tagId: Int): Single<Tag>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(tag: Tag)

    @Delete
    fun deleteTag(tag: Tag)
}