package fish.payara.library.pleasync.model;

public interface EventEnvelope {
    /**
     * Representation of the entity event relates to. Useful for filtering or sharding
     * @return
     */
    String getBusinessKey();

    /**
     * Representation of Event Type. Simple class name is a good candidate
     * @return
     */
    String getEventType();

    /**
     * Event type in combination with payload version determine interpretation of payload.
     * This allows for backwards-incompatible changes in event structure, or even in serialization type.
     * Producer can then
     * @return payload version
     */
    int getPayloadVersion();

    /**
     * Binary payload representing the event. Application shall use {@link EventDeserializer} to extract the event object
     * @return payload
     */
    byte[] getPayload();
}
