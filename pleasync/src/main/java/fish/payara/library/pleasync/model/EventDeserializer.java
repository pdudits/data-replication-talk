package fish.payara.library.pleasync.model;

public abstract class EventDeserializer<T> {
    /**
     * Return application-specific representation of an envelope
     * @param envelope
     * @return
     */
    public abstract T deserialize(EventEnvelope envelope);

    protected abstract Class<? extends T> determineClass(String businessKey, String eventType, int payloadVersion);
}
