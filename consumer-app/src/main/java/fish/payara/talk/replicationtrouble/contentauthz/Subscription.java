package fish.payara.talk.replicationtrouble.contentauthz;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Subscription {
    @Id
    String id;

    @ElementCollection
    Set<String> contents;

    protected Subscription() {

    }

    public String getId() {
        return id;
    }

    public Subscription(String subscriptionId) {
        this.id = Objects.requireNonNull(subscriptionId, "subscription Id is required");
    }

    void addContent(String id) {
        if (this.contents == null) {
            this.contents = new HashSet<>();
        }
        this.contents.add(id);
    }

}
