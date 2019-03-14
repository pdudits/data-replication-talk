package fish.payara.library.pleasync.jpa;

import fish.payara.library.pleasync.model.EventEnvelope;
import fish.payara.library.pleasync.model.EventStoreWriter;

import javax.persistence.EntityManager;
import java.util.function.Function;
import java.util.function.Supplier;

public class EventEntityWriter<E extends EventEntity> extends ComposingJpaWriter<E, Long> {

    public EventEntityWriter(EntityManager mgr, Supplier<E> entityConstructor) {
        super(mgr, entityMaker(entityConstructor), EventEntity::getId);
    }

    static <E extends EventEntity> Function<EventEnvelope, E> entityMaker(Supplier<E> entityConstructor) {
        return envelope -> {
            E entity = entityConstructor.get();
            entity.copyFrom(envelope);
            return entity;
        };
    }
}
