package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import java.time.Instant;

@Path("/replication/info")
public class ReplicationInfoResource {
    @PersistenceContext
    EntityManager em;

    @GET
    public Info info() {
        Cursor cursor = em.find(Cursor.class, Cursor.USERS);
        if (cursor == null) {
            throw new NotFoundException();
        } else {
            return new Info(cursor);
        }
    }

    public static class Info {
        public Instant lastSync;
        public Long lastId;

        Info(Cursor cursor) {
            this.lastSync = cursor.lastSync;
            this.lastId = cursor.lastId;
        }
    }
}
