package fish.payara.library.pleasync.jpa;

import fish.payara.library.pleasync.model.EventChunk;
import fish.payara.library.pleasync.model.EventStoreReader;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.BiFunction;

public class EventEntityReader<E extends EventEntity> implements EventStoreReader {

    private final EntityManager mgr;
    private final Class<E> entityClass;

    public EventEntityReader(EntityManager mgr, Class<E> entityClass) {
        this.mgr = mgr;
        this.entityClass = entityClass;
    }

    @Override
    public EventChunk read(String cursor, int pageSize) {
        if (cursor == null) {
            return queryFromBeginning(pageSize);
        } else {
            return queryFromPosition(cursor, pageSize);
        }
    }

    private EventChunk queryFromPosition(String cursor, int pageSize) {
        CriteriaQuery<E> q = cursorQuery(Long.parseLong(cursor));
        return makeChunk(q, pageSize);
    }

    private EventChunk queryFromBeginning(int pageSize) {
        CriteriaQuery<E> q = baseQuery();
        return makeChunk(q, pageSize);
    }

    private EventChunk makeChunk(CriteriaQuery<E> q, int pageSize) {
        List<E> events = mgr.createQuery(q).setMaxResults(pageSize+1).getResultList();
        return makeChunk(events, pageSize);
    }

    private EventChunk makeChunk(List<E> events, int pageSize) {
        boolean isTail = events.size() <= pageSize;
        List<E> cropped = isTail ? events : events.subList(0, pageSize - 1);
        long cursor = cropped.get(cropped.size() - 1).getId();
        return new EventChunk(isTail, cropped.size(), cropped);
    }

    private CriteriaQuery<E> baseQuery() {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<E> q = cb.createQuery(entityClass);
        Root<E> root = q.from(entityClass);
        q.select(root).orderBy(cb.asc(root.get("id")));
        return q;
    }

    private CriteriaQuery<E> cursorQuery(long cursor) {
        CriteriaBuilder cb = mgr.getCriteriaBuilder();
        CriteriaQuery<E> q = cb.createQuery(entityClass);
        Root<E> root = q.from(entityClass);
        q.select(root).orderBy(cb.asc(root.get("id")));
        q.where(cb.gt(root.get("id"), cursor));
        return q;
    }

}
