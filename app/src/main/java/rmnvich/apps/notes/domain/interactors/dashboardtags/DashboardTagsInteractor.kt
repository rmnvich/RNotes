package rmnvich.apps.notes.domain.interactors.dashboardtags

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import rmnvich.apps.notes.data.common.Constants.DEFAULT_DELAY
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.TagsRepository
import java.util.concurrent.TimeUnit

class DashboardTagsInteractor(
        private val tagsRepository: TagsRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getAllTags(): Flowable<List<Tag>> {
        return tagsRepository.getAllTags()
                .subscribeOn(schedulersProvider.io())
                .delay(DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }

    fun insertTag(tag: Tag): Completable {
        return tagsRepository.insertTag(tag)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun updateTag(tagId: Int, tagName: String): Completable {
        return tagsRepository.updateTag(tagId, tagName)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }

    fun deleteTag(tag: Tag): Completable {
        return tagsRepository.deleteTag(tag)
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.ui())
    }
}