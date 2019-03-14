package fish.payara.library.pleasync.producer;

import fish.payara.library.pleasync.model.EventEnvelope;
import fish.payara.library.pleasync.model.EventSerializer;
import fish.payara.library.pleasync.model.EventStoreWriter;

/**
 *
 * @param <E> base type of replication event
 * @param <S> Storage-specific representation of stored event
 */
public class EventCollector<E, S> {

    private final EventSerializer<? super E> serializer;
    private final EventStoreWriter<S> writer;

    public EventCollector(EventSerializer<? super E> serializer, EventStoreWriter<S> writer) {
        this.serializer = serializer;
        this.writer = writer;
    }

    public S process(E event) {
        EventEnvelope envelope = serializer.serialize(event);
        return writer.storeEvent(envelope);
    }
}
