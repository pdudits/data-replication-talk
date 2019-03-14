package fish.payara.library.pleasync.model;

public class DefaultEventEnvelope implements EventEnvelope {

    private String businessKey;

    private String eventType;

    private int payloadVersion;

    private byte[] payload;

    public DefaultEventEnvelope(String businessKey, String eventType, int payloadVersion, byte[] payload) {
        this.businessKey = businessKey;
        this.eventType = eventType;
        this.payloadVersion = payloadVersion;
        this.payload = payload;
    }

    @Override
    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public int getPayloadVersion() {
        return payloadVersion;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }
}
