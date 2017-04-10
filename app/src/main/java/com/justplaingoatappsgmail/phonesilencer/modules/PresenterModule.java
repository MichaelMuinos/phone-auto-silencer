package com.justplaingoatappsgmail.phonesilencer.modules;

import android.content.Context;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.presenters.EventPostPresenter;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class PresenterModule {

    @Provides
    public EventPostPresenter provideEventPostPresenter(Realm realm, Context context, EventPostContract.View view) {
        return new EventPostPresenter(realm, context, view);
    }

}
