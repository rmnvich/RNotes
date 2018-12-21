package rmnvich.apps.notes.di.app;

import android.arch.persistence.room.Room;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import rmnvich.apps.notes.data.repositories.datasource.Database;
import rmnvich.apps.notes.di.dashboard.DashboardComponent;
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder;
import rmnvich.apps.notes.di.global.scope.PerApplication;
import rmnvich.apps.notes.presentation.ui.fragment.dashboard.DashboardFragment;

import static rmnvich.apps.notes.data.common.Constants.DATABASE_NAME;

@Module(subcomponents = {DashboardComponent.class})
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
    @ClassKey(DashboardFragment.class)
    BaseComponentBuilder provideDashboardFragment(DashboardComponent.Builder builder) {
        return builder;
    }
}