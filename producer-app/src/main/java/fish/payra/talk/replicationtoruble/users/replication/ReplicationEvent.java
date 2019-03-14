package fish.payra.talk.replicationtoruble.users.replication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ReplicationEvent {
    @Id
    @GeneratedValue
    long id;

    @Column
    String eventType;

    @Column
    String payload;
}
