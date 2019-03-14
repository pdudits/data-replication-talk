package fish.payara.library.pleasync.jpa;

import fish.payara.library.pleasync.model.EventEnvelope;
import fish.payara.library.pleasync.model.EventStoreWriter;

import javax.persistence.EntityManager;
import java.util.function.Function;

public class ComposingJpaWriter<E, T> implements EventStoreWriter<T> {
    private EntityManager mgr;
    private Function<EventEnvelope, E> entityMaker;
    private Function<E, T> idExtractor;

    public ComposingJpaWriter(EntityManager mgr, Function<EventEnvelope, E> entityMaker, Function<E, T> idExtractor) {
        this.mgr = mgr;
        this.entityMaker = entityMaker;
        this.idExtractor = idExtractor;
    }

    @Override
    public T storeEvent(EventEnvelope envelope) {
        E entity = entityMaker.apply(envelope);
        mgr.persist(entity);
        return idExtractor.apply(entity);
    }
}
