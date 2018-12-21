package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.TagsRepository

class TagsRepositoryImpl(database: Database) : TagsRepository {

    private val tagDao = database.tagDao()

    override fun getAllTags(): Flowable<List<Tag>> {
        return tagDao.getAllTags()
    }

    override fun getTagById(tagId: Int): Single<Tag> {
        return tagDao.getTagById(tagId)
    }

    override fun insertOrUpdateTag(tag: Tag): Completable {
        return Completable.fromAction { tagDao.insertTag(tag) }
    }

    override fun deleteTag(tag: Tag): Completable {
        return Completable.fromAction { tagDao.deleteTag(tag) }
    }
}