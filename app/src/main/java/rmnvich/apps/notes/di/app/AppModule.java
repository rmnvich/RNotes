package rmnvich.apps.notes.di.app;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import rmnvich.apps.notes.di.dashboard.DashboardComponent;
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder;
import rmnvich.apps.notes.di.global.scope.PerApplication;
import rmnvich.apps.notes.presentation.ui.fragment.dashboard.DashboardFragment;

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

    @Provides
    @IntoMap
    @ClassKey(DashboardFragment.class)
    BaseComponentBuilder provideDashboardFragment(DashboardComponent.Builder builder) {
        return builder;
    }
}