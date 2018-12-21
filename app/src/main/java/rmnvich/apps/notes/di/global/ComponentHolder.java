package rmnvich.apps.notes.di.global;

import android.content.Context;
import rmnvich.apps.notes.di.app.AppComponent;
import rmnvich.apps.notes.di.app.AppModule;
import rmnvich.apps.notes.di.app.DaggerAppComponent;
import rmnvich.apps.notes.di.global.base.BaseComponent;
import rmnvich.apps.notes.di.global.base.BaseComponentBuilder;
import rmnvich.apps.notes.di.global.base.BaseModule;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

public class ComponentHolder {

    private final Context mContext;

    @Inject
    Map<Class<?>, Provider<BaseComponentBuilder>> mBuilders;

    private Map<Class<?>, BaseComponent> mComponents;

    public ComponentHolder(Context mContext) {
        this.mContext = mContext;
    }

    public void init() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(mContext)).build();
        appComponent.injectComponentsHolder(this);
        mComponents = new HashMap<>();
    }

    public BaseComponent getComponent(Class<?> cls, BaseModule module) {
        BaseComponent component = mComponents.get(cls);
        if (component == null) {
            BaseComponentBuilder builder = mBuilders.get(cls).get();
            if (module != null) {
                builder.module(module);
            }
            component = builder.build();
            mComponents.put(cls, component);
        }
        return component;
    }

    public BaseComponent getComponent(Class<?> cls) {
        return getComponent(cls, null);
    }

    public void releaseComponent(Class<?> cls) {
        mComponents.put(cls, null);
    }

}
