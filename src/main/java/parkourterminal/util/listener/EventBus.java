package parkourterminal.util.listener;

import parkourterminal.util.SystemOutHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private Map<String, List<EventListener>> listeners = new HashMap<String, List<EventListener>>();

    public void register(String eventType, EventListener listener) {
        List<EventListener> list = listeners.get(eventType);
        if (list == null) {
            list = new ArrayList<EventListener>();
            listeners.put(eventType, list);
        }
        list.add(listener);
    }

    public void emit(String eventType, Object data) {
        if (listeners.containsKey(eventType)) {
            for (EventListener listener : listeners.get(eventType)) {
                listener.onEvent(eventType, data);
            }
        }
        SystemOutHelper.printf("emit");
    }
}