package com.justplaingoatappsgmail.phonesilencer.dagger;

import com.justplaingoatappsgmail.phonesilencer.dagger.modules.ApplicationModule;
import com.justplaingoatappsgmail.phonesilencer.dagger.modules.PresenterModule;
import com.justplaingoatappsgmail.phonesilencer.dagger.modules.RealmModule;
import com.justplaingoatappsgmail.phonesilencer.view.activities.EventListActivity;
import com.justplaingoatappsgmail.phonesilencer.view.activities.EventPostActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Defining all of our injection targets.
 */
@Singleton
@Component(modules = {ApplicationModule.class, PresenterModule.class, RealmModule.class})
public interface ApplicationComponent {
    void inject(EventListActivity target);
    void inject(EventPostActivity target);
}
