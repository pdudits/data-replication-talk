package fish.payara.library.pleasync.jsonb;

import fish.payara.library.pleasync.model.EventDeserializer;
import fish.payara.library.pleasync.model.EventEnvelope;

import javax.json.bind.Jsonb;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public abstract class JsonbDeserializer<T> extends EventDeserializer<T> {
    private Jsonb jsonb;

    public JsonbDeserializer(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @Override
    public T deserialize(EventEnvelope envelope) {
        Class<? extends T> clazz = determineClass(envelope.getBusinessKey(), envelope.getEventType(), envelope.getPayloadVersion());
        try (ByteArrayInputStream is = new ByteArrayInputStream(envelope.getPayload())) {
            return jsonb.fromJson(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException("ByteArrayInputStream should not be throwing IOExceptions",e);
        }
    }
}
