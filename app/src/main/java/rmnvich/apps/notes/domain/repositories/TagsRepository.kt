package rmnvich.apps.notes.domain.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.domain.entity.Tag

interface TagsRepository {

    fun getAllTags(): Flowable<List<Tag>>

    fun updateTag(tagId: Int, tagName: String): Completable

    fun insertTag(tag: Tag): Completable

    fun deleteTag(tag: Tag): Completable
}