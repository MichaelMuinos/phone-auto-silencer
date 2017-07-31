package com.justplaingoatappsgmail.phonesilencer.dagger.modules;

import com.justplaingoatappsgmail.phonesilencer.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencer.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencer.model.database.RealmService;
import com.justplaingoatappsgmail.phonesilencer.presenter.BootBroadcastReceiverPresenter;
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
    public EventListContract.Presenter provideEventListPresenter(RealmService realmService, AtomicInteger atomicInteger) {
        return new EventListPresenter(realmService, atomicInteger);
    }

    @Provides
    public EventPostContract.Presenter provideEventPostPresenter(RealmService realmService) {
        return new EventPostPresenter(realmService);
    }

    @Provides
    public SetRingerServicePresenter provideSetRingerServicePresenter(RealmService realmService, AtomicInteger atomicInteger) {
        return new SetRingerServicePresenter(realmService, atomicInteger);
    }

    @Provides
    public SetNormalServicePresenter provideSetNormalServicePresenter(RealmService realmService) {
        return new SetNormalServicePresenter(realmService);
    }

    @Provides
    public BootBroadcastReceiverPresenter provideBootBroadcastReceiverPresenter(RealmService realmService) {
        return new BootBroadcastReceiverPresenter(realmService);
    }

}
