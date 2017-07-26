package com.justplaingoatappsgmail.phonesilencer.dagger.modules;

import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class UtilModule {

    @Provides
    @Singleton
    public AtomicInteger provideAtomicInteger() {
        return new AtomicInteger();
    }

}
