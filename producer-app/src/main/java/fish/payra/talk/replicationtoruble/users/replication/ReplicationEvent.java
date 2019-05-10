package fish.payra.talk.replicationtoruble.users.replication;

import fish.payra.talk.replicationtoruble.users.events.UserEvent;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.annotation.JsonbTypeSerializer;
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
    @JsonbTypeSerializer(FlattenSerializer.class)
    public String payload;

    protected ReplicationEvent() {
        // for JPA
    }

    public ReplicationEvent(UserEvent event) {
        this.eventType = event.getClass().getSimpleName();
        this.payload = JsonbBuilder.create().toJson(event);
    }

    static TypedQuery<ReplicationEvent> fetchPage(EntityManager mgr, long id, int size) {
        return mgr.createNamedQuery("ReplicationEvent.page", ReplicationEvent.class)
                .setParameter("id", id)
                .setMaxResults(size);
    }
}
