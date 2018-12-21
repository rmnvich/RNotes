package rmnvich.apps.notes.data.repositories

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import rmnvich.apps.notes.data.common.Constants.DEFAULT_DELAY
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.TagsRepository
import java.util.concurrent.TimeUnit

class TagsRepositoryImpl(database: Database) : TagsRepository {

    private val tagDao = database.tagDao()

    override fun getAllTags(): Flowable<List<Tag>> {
        return tagDao.getAllTags()
                .subscribeOn(Schedulers.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTagById(tagId: Int): Single<Tag> {
        return tagDao.getTagById(tagId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun insertTag(tag: Tag): Completable {
        return Completable.fromAction {
            tagDao.insertTag(tag)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteTag(tag: Tag): Completable {
        return Completable.fromAction {
            tagDao.deleteTag(tag)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}