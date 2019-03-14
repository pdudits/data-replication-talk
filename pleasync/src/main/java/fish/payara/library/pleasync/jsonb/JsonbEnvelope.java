package fish.payara.library.pleasync.jsonb;

import fish.payara.library.pleasync.model.EventEnvelope;

public class JsonbEnvelope implements EventEnvelope {


    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public String getEventType() {
        return null;
    }

    @Override
    public int getPayloadVersion() {
        return 0;
    }

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }

}
