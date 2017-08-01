package com.justplaingoatappsgmail.phonesilencerpremium.dagger.modules;

import com.justplaingoatappsgmail.phonesilencerpremium.contracts.EventListContract;
import com.justplaingoatappsgmail.phonesilencerpremium.contracts.EventPostContract;
import com.justplaingoatappsgmail.phonesilencerpremium.model.database.RealmService;
import com.justplaingoatappsgmail.phonesilencerpremium.presenter.BootBroadcastReceiverPresenter;
import com.justplaingoatappsgmail.phonesilencerpremium.presenter.EventListPresenter;
import com.justplaingoatappsgmail.phonesilencerpremium.presenter.EventPostPresenter;
import com.justplaingoatappsgmail.phonesilencerpremium.presenter.SetNormalServicePresenter;
import com.justplaingoatappsgmail.phonesilencerpremium.presenter.SetRingerServicePresenter;
import java.util.concurrent.atomic.AtomicInteger;

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
