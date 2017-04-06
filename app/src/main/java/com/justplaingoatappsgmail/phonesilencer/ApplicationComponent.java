package com.justplaingoatappsgmail.phonesilencer;

import com.justplaingoatappsgmail.phonesilencer.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/*
    Defining all of our injection targets.
 */
@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(PhoneSilencerApplication target);
}
