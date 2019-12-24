package com.example;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.ReplaySubject;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.ObservesAsync;
import java.time.LocalDateTime;

@ApplicationScoped
public class ActivityStreams {

    ReplaySubject<JsonObject> replaySubject;
    Flowable<JsonObject> flowable;

    @PostConstruct public void init() {
        replaySubject = ReplaySubject.create();
        flowable = replaySubject.share().toFlowable(BackpressureStrategy.BUFFER);
    }

    public void onActivityCreated(@ObservesAsync Activity activity) {
        replaySubject.onNext(JsonObject.mapFrom(activity));
    }

    @Outgoing("activitiesOut")
    public Publisher<JsonObject> onReceivedActivityCreated() {
        return flowable;
    }

    @Incoming("activities")
    @Outgoing("my-data-stream")
    @Broadcast
    public Activity onActivityReceived(JsonObject data) {
        Activity activity = data.mapTo(Activity.class);
        activity.setOccurred(LocalDateTime.now());
        return activity;
    }

}
