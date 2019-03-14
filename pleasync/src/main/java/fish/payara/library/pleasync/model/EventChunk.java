package fish.payara.library.pleasync.model;

import java.util.Collections;
import java.util.List;

public class EventChunk {
    private boolean onTail;
    private int count;
    private List<EventEnvelope> events;

    public EventChunk(boolean onTail, int count, List<? extends EventEnvelope> events) {
        this.onTail = onTail;
        this.count = count;
        this.events = Collections.unmodifiableList(events);
    }

    public boolean isOnTail() {
        return onTail;
    }

    public int getCount() {
        return count;
    }

    public List<EventEnvelope> getEvents() {
        return events;
    }
}
