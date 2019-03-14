package fish.payara.library.pleasync.model;

public interface EventStoreWriter<T> {
    /**
     * Store the event.
     * @param envelope the envelope of event to store
     * @return Store-specific representation of the event or sequence, or null if neither is supported
     */
    T storeEvent(EventEnvelope envelope);

}
