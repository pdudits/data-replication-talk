package fish.payara.talk.replicationtrouble.contentauthz;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "CONTENT_SUBSCRIPTION")
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

    public Set<String> getContents() {
        return contents;
    }

    public Subscription(String subscriptionId) {
        this.id = Objects.requireNonNull(subscriptionId, "subscription Id is required");
    }

    boolean addContent(String id) {
        if (this.contents == null) {
            this.contents = new HashSet<>();
        }
        return this.contents.add(id);
    }

    public boolean removeContent(String contentId) {
        if (this.contents == null) {
            return false;
        }
        return this.contents.remove(contentId);
    }
}
