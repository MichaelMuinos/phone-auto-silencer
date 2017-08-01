package com.justplaingoatappsgmail.phonesilencerpremium.dagger.components;

import com.justplaingoatappsgmail.phonesilencerpremium.dagger.modules.ApplicationModule;
import com.justplaingoatappsgmail.phonesilencerpremium.dagger.modules.PresenterModule;
import com.justplaingoatappsgmail.phonesilencerpremium.dagger.modules.RealmModule;
import com.justplaingoatappsgmail.phonesilencerpremium.model.receivers.BootBroadcastReceiver;
import com.justplaingoatappsgmail.phonesilencerpremium.view.activities.EventListActivity;
import com.justplaingoatappsgmail.phonesilencerpremium.view.activities.EventPostActivity;
import com.justplaingoatappsgmail.phonesilencerpremium.model.services.SetNormalService;
import com.justplaingoatappsgmail.phonesilencerpremium.model.services.SetRingerService;

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
    void inject(SetRingerService target);
    void inject(SetNormalService target);
    void inject(BootBroadcastReceiver target);
}
