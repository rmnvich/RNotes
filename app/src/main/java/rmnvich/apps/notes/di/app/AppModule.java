package rmnvich.apps.notes.di.app;

import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import rmnvich.apps.notes.data.repositories.datasource.Database;
import rmnvich.apps.notes.di.addeditnote.AddEditNoteComponent;
import rmnvich.apps.notes.di.dashboardnotes.DashboardNotesComponent;
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder;
import rmnvich.apps.notes.di.global.scope.PerApplication;
import rmnvich.apps.notes.presentation.ui.fragment.addeditnote.AddEditNoteFragment;
import rmnvich.apps.notes.presentation.ui.fragment.dashboardnotes.DashboardNotesFragment;

import static rmnvich.apps.notes.data.common.Constants.DATABASE_NAME;

@Module(subcomponents = {DashboardNotesComponent.class, AddEditNoteComponent.class})
public class AppModule {

    private final Context mContext;

    public AppModule(Context mContext) {
        this.mContext = mContext;
    }

    @PerApplication
    @Provides
    Context provideContext() {
        return mContext;
    }

    @PerApplication
    @Provides
    Database provideDatabase() {
        return Room.databaseBuilder(mContext, Database.class, DATABASE_NAME).build();
    }

    @Provides
    @IntoMap
    @ClassKey(DashboardNotesFragment.class)
    BaseComponentBuilder provideDashboardFragment(DashboardNotesComponent.Builder builder) {
        return builder;
    }

    @Provides
    @IntoMap
    @ClassKey(AddEditNoteFragment.class)
    BaseComponentBuilder provideAddEditNoteFragment(AddEditNoteComponent.Builder builder) {
        return builder;
    }
}