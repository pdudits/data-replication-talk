package fish.payara.library.pleasync.jsonb;

import fish.payara.library.pleasync.model.DefaultEventEnvelope;
import fish.payara.library.pleasync.model.EventEnvelope;
import fish.payara.library.pleasync.model.EventSerializer;

import javax.json.bind.Jsonb;
import java.io.ByteArrayOutputStream;
import java.util.function.Function;

public class JsonbSerializer<T> extends EventSerializer<T> {
    private final Jsonb serializer;
    private Function<T, String> businessKeyExtractor;

    public JsonbSerializer(Jsonb serializer, Function<T, String> businessKeyExtractor) {
        this.serializer = serializer;
        this.businessKeyExtractor = businessKeyExtractor;
    }

    @Override
    public EventEnvelope serialize(T event) {
        String businessKey = determineBusinessKey(event);
        String eventType = determineEventType(event);
        int payloadVersion = determinePayloadVersion(event);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        serializer.toJson(event, bos);
        return new DefaultEventEnvelope(businessKey, eventType, payloadVersion, bos.toByteArray());
    }

    protected String determineBusinessKey(T event) {
        return businessKeyExtractor.apply(event);
    }
}
