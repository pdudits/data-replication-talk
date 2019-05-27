package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import fish.payara.talk.replicationtrouble.contentauthz.users.Users;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.List;


@Singleton
public class UserDataPoller {
    @Inject
    Users processor;

    @Inject
    @RestClient
    ReplicationAPI endpoint;

    @PersistenceContext
    EntityManager em;

    @Inject
    Jsonb jsonb;

    public void poll() {
        Cursor cursor = em.find(Cursor.class, Cursor.USERS);

        if (cursor == null) {
            cursor = new Cursor();
            em.persist(cursor);
        }
        fetchAndProcess(cursor);
    }

    void fetchAndProcess(Cursor cursor) {
        cursor.lastSync = Instant.now();

        List<ReplicationEvent> events = endpoint.fetch(cursor.lastId, 500);

        cursor.lastEvents = events.size();
        events.forEach(event -> event.apply(cursor, jsonb, processor));
    }
}
