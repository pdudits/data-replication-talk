package fish.payra.talk.replicationtoruble.users.replication;

import fish.payra.talk.replicationtoruble.users.events.UserEvent;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.String.format;

@RequestScoped
class EventCollector {
    private static final Logger logger = Logger.getLogger(EventCollector.class.getName());
    @PersistenceContext
    EntityManager mgr;

    @Inject
    Jsonb jsonb;

    @Transactional(Transactional.TxType.MANDATORY)
    public void storeEvent(@Observes UserEvent event) {
        ReplicationEvent entity = new ReplicationEvent();
        entity.eventType = event.getClass().getSimpleName();
        entity.payload = jsonb.toJson(event);
        logger.info( () -> format("Storing event %s %s", entity.eventType, entity.payload));
        mgr.persist(entity);
    }
}
