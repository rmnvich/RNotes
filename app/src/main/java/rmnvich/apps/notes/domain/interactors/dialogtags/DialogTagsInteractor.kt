package rmnvich.apps.notes.domain.interactors.dialogtags

import io.reactivex.Flowable
import rmnvich.apps.notes.data.common.Constants
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.domain.entity.Tag
import rmnvich.apps.notes.domain.repositories.TagsRepository
import java.util.concurrent.TimeUnit

class DialogTagsInteractor(
        private val tagsRepository: TagsRepository,
        private val schedulersProvider: SchedulersProvider
) {

    fun getAllTags(): Flowable<List<Tag>> {
        return tagsRepository.getAllTags()
                .subscribeOn(schedulersProvider.io())
                .delay(Constants.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.ui())
    }
}