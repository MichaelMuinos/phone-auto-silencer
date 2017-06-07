package com.justplaingoatappsgmail.phonesilencer.dagger.modules;

import android.content.Context;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module(includes = {ApplicationModule.class})
public class RealmModule {

    @Provides
    public RealmService provideRealmService(Realm realm, Context context) {
        return new RealmService(realm, context);
    }

    @Provides
    public Realm provideRealmInstance() {
        return Realm.getDefaultInstance();
    }

}
