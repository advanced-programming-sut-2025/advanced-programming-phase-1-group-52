package com.example.main.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple, thread-safe, singleton event bus for decoupled communication.
 */
public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final Map<Class<? extends AppEvent>, List<Consumer<AppEvent>>> listeners = new HashMap<>();

    private EventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * Subscribes a listener to a specific type of event.
     * @param eventClass The class of the event to listen for.
     * @param listener The action to execute when the event is published.
     */
    public <T extends AppEvent> void subscribe(Class<T> eventClass, Consumer<T> listener) {
        synchronized (listeners) {
            List<Consumer<AppEvent>> eventListeners = listeners.computeIfAbsent(eventClass, k -> new ArrayList<>());
            eventListeners.add((Consumer<AppEvent>) listener);
        }
    }

    /**
     * Publishes an event to all registered listeners.
     * @param event The event to publish.
     */
    public void publish(AppEvent event) {
        List<Consumer<AppEvent>> eventListeners;
        synchronized (listeners) {
            eventListeners = listeners.get(event.getClass());
            if (eventListeners == null) {
                return;
            }
            eventListeners = new ArrayList<>(eventListeners);
        }

        for (Consumer<AppEvent> listener : eventListeners) {
            listener.accept(event);
        }
    }
}
