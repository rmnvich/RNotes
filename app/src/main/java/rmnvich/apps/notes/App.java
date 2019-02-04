package rmnvich.apps.notes;

import android.app.Application;
import android.content.Context;

import rmnvich.apps.notes.di.global.ComponentHolder;
import timber.log.Timber;

public class App extends Application {

    private ComponentHolder componentsHolder;

    public static App getApp(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new FileLoggingTree(getApplicationContext()));
        Timber.d("------------------------------------");

        componentsHolder = new ComponentHolder(this);
        componentsHolder.init();
    }

    public ComponentHolder getComponentsHolder() {
        return componentsHolder;
    }
}
