package com.reactLab.kpi.configs;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class RecentChangesEventPublisher implements
        ApplicationListener<RecentChangesEvent>, // <1>
        Consumer<FluxSink<RecentChangesEvent>> { //<2>

    private final Executor executor;
    private final BlockingQueue<RecentChangesEvent> queue =
            new LinkedBlockingQueue<>(); // <3>

    RecentChangesEventPublisher(Executor executor) {
        this.executor = executor;
    }

    // <4>
    @Override
    public void onApplicationEvent(RecentChangesEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<RecentChangesEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    RecentChangesEvent event = queue.take(); // <5>
                    sink.next(event); // <6>
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}