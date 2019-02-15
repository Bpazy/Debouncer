package com.github.bpazy.debouncer;

import java.util.concurrent.*;

/**
 * @author ziyuan
 */
public class Debouncer {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentHashMap<Object, Future<?>> delayedMap = new ConcurrentHashMap<>();

    public void debounce(final Object key, final Runnable runnable, long delay, TimeUnit unit) {
        if (scheduler.isShutdown()) throw new RuntimeException("debouncer has been shutdown");

        final Future<?> prev = delayedMap.put(key, scheduler.schedule(() -> {
            try {
                runnable.run();
            } finally {
                delayedMap.remove(key);
            }
        }, delay, unit));
        if (prev != null) {
            prev.cancel(true);
        }
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}
