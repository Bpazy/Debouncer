package com.github.bpazy.debouncer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author ziyuan
 */
public class DebouncerTest {
    private static final String key1 = "key1";
    private static final String key2 = "key2";

    @Test
    public void testDebounce() {
        Debouncer d1 = new Debouncer();

        // The first task to throw end RuntimeException. It should not be invoked.
        d1.debounce(key1, () -> {
            throw new RuntimeException("this should not be invoked");
        }, 1, SECONDS);

        // The second empty task to replace previous task.
        d1.debounce(key1, () -> {
            // Empty
        }, 1, SECONDS);

        new Thread(d1::shutdown).start();
    }

    @Test
    public void testShutdown() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Debouncer d1 = new Debouncer();
            d1.shutdown();
            d1.debounce(key2, () -> {
                // Empty
            }, 1, SECONDS);
        });
    }
}