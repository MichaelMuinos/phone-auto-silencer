package com.justplaingoatappsgmail.phonesilencer.dagger.modules;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import com.justplaingoatappsgmail.phonesilencer.presenter.EventListPresenter;
import com.justplaingoatappsgmail.phonesilencer.presenter.EventPostPresenter;
import com.justplaingoatappsgmail.phonesilencer.presenter.SetNormalServicePresenter;
import com.justplaingoatappsgmail.phonesilencer.presenter.SetRingerServicePresenter;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(includes = {RealmModule.class, UtilModule.class})
public class PresenterModule {

    @Provides
    @Singleton
    public EventListContract.Presenter provideEventListPresenter(RealmService realmService, AtomicInteger atomicInteger) {
        return new EventListPresenter(realmService, atomicInteger);
    }

    @Provides
    @Singleton
    public EventPostContract.Presenter provideEventPostPresenter(RealmService realmService) {
        return new EventPostPresenter(realmService);
    }

    /**
     * Not a singleton because the presenter could be created multiple times by the service
     */
    @Provides
    public SetRingerServicePresenter provideSetRingerServicePresenter(RealmService realmService, AtomicInteger atomicInteger) {
        return new SetRingerServicePresenter(realmService, atomicInteger);
    }

    /**
     * Not a singleton because the presenter could be created multiple times by the service
     */
    @Provides
    public SetNormalServicePresenter provideSetNormalServicePresenter(RealmService realmService) {
        return new SetNormalServicePresenter(realmService);
    }

}
