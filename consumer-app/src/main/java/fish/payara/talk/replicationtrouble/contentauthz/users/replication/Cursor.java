package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
class Cursor {
    public static final String USERS = "users";
    @Id
    String system = USERS; // we only have one target system yet

    Instant lastSync = Instant.now();
    int lastEvents;
    Long lastId;
}
