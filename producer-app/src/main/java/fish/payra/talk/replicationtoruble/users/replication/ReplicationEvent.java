package fish.payra.talk.replicationtoruble.users.replication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

@Entity
@NamedQuery(name = "ReplicationEvent.page",
    query = "select e from ReplicationEvent e where e.id > :id order by e.id asc")
public class ReplicationEvent {
    @Id
    @GeneratedValue
    public long id;

    @Column
    public String eventType;

    @Column
    public String payload;

    static TypedQuery<ReplicationEvent> fetchPage(EntityManager mgr, long id, int size) {
        return mgr.createNamedQuery("ReplicationEvent.page", ReplicationEvent.class)
                .setParameter("id", id)
                .setMaxResults(size);
    }
}
