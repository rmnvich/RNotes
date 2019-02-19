package rmnvich.apps.notes.di.addeditnote

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import rmnvich.apps.notes.data.common.SchedulersProvider
import rmnvich.apps.notes.data.repositories.FileRepositoryImpl
import rmnvich.apps.notes.data.repositories.NotesRepositoryImpl
import rmnvich.apps.notes.data.repositories.TagsRepositoryImpl
import rmnvich.apps.notes.data.repositories.datasource.Database
import rmnvich.apps.notes.di.global.base.BaseModule
import rmnvich.apps.notes.di.global.scope.PerFragment
import rmnvich.apps.notes.domain.interactors.addeditnote.AddEditNoteInteractor
import rmnvich.apps.notes.domain.interactors.dialogtags.DialogTagsInteractor
import rmnvich.apps.notes.domain.repositories.FileRepository
import rmnvich.apps.notes.domain.repositories.NotesRepository
import rmnvich.apps.notes.domain.repositories.TagsRepository
import rmnvich.apps.notes.domain.utils.ViewModelFactory
import rmnvich.apps.notes.presentation.ui.custom.ColorPickerDialog
import rmnvich.apps.notes.presentation.ui.dialog.DialogMoreActions
import rmnvich.apps.notes.presentation.ui.dialog.DialogTags

@Module
class AddEditNoteModule(private val application: Application,
                        private val context: Context
) : BaseModule {

    @PerFragment
    @Provides
    fun provideViewModelFactory(interactor: AddEditNoteInteractor): ViewModelFactory {
        return ViewModelFactory(interactor, application)
    }

    @PerFragment
    @Provides
    fun provideAddEditNoteInteractor(notesRepository: NotesRepository,
                                     fileRepository: FileRepository,
                                     schedulersProvider: SchedulersProvider): AddEditNoteInteractor {
        return AddEditNoteInteractor(notesRepository, fileRepository, schedulersProvider)
    }

    @PerFragment
    @Provides
    fun provideDialogTagsInteractor(tagsRepository: TagsRepository,
                                    schedulersProvider: SchedulersProvider): DialogTagsInteractor {
        return DialogTagsInteractor(tagsRepository, schedulersProvider)
    }

    @PerFragment
    @Provides
    fun provideSchedulersProvider(): SchedulersProvider {
        return SchedulersProvider()
    }

    @PerFragment
    @Provides
    fun provideFileRepository(): FileRepository {
        return FileRepositoryImpl(application)
    }

    @PerFragment
    @Provides
    fun provideNotesRepository(database: Database): NotesRepository {
        return NotesRepositoryImpl(database)
    }

    @PerFragment
    @Provides
    fun provideTagsRepository(database: Database): TagsRepository {
        return TagsRepositoryImpl(database)
    }

    @Provides
    fun provideColorPickerDialog(): ColorPickerDialog.Builder {
        return ColorPickerDialog.newBuilder()
                .setAllowCustom(false)
                .setShowAlphaSlider(false)
                .setAllowPresets(false)
                .setShowColorShades(false)
    }

    @Provides
    fun provideTagsDialog(dialogTagsInteractor: DialogTagsInteractor): DialogTags {
        return DialogTags(dialogTagsInteractor, context)
    }

    @Provides
    fun provideActionsMoreDialog(): DialogMoreActions {
        return DialogMoreActions(context)
    }
}