package fish.payara.library.pleasync.jpa;

import fish.payara.library.pleasync.model.EventEnvelope;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class EventEntity implements EventEnvelope {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String businessKey;

    @Column
    private String eventType;

    @Column
    private int payloadVersion;

    @Lob
    private byte[] payload;

    public long getId() {
        return id;
    }

    @Override
    public String getBusinessKey() {
        return businessKey != null ? businessKey : "";
    }

    @Override
    public String getEventType() {
        return eventType != null ? eventType : "";
    }

    @Override
    public int getPayloadVersion() {
        return payloadVersion;
    }

    @Override
    public byte[] getPayload() {
        return payload != null ? payload : new byte[0];
    }

    protected void copyFrom(EventEnvelope envelope) {
        this.businessKey = envelope.getBusinessKey();
        this.eventType = envelope.getEventType();
        this.payloadVersion = envelope.getPayloadVersion();
        this.payload = envelope.getPayload();
    }
}
