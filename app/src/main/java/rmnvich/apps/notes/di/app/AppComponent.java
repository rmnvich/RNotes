package rmnvich.apps.notes.di.app;

import dagger.Component;
import rmnvich.apps.notes.di.global.ComponentHolder;
import rmnvich.apps.notes.di.global.scope.PerApplication;

@PerApplication
@Component(modules = AppModule.class)
public interface AppComponent {
    void injectComponentsHolder(ComponentHolder holder);
}
