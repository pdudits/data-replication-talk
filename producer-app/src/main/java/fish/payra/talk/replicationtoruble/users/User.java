package fish.payra.talk.replicationtoruble.users;

import fish.payra.talk.replicationtoruble.users.events.SubscriptionAdded;
import fish.payra.talk.replicationtoruble.users.events.SubscriptionRemoved;
import fish.payra.talk.replicationtoruble.users.events.UserCreated;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

@Entity
public class User {
    @Id
    String id;

    @Column
    String name;

    @ElementCollection
    Set<String> subscriptions;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getSubscriptions() {
        return subscriptions;
    }

    UserCreated created() {
        UserCreated event = new UserCreated();
        event.userId = id;
        return event;
    }

    public SubscriptionAdded subscriptionAdded(String roleName) {
        SubscriptionAdded event = new SubscriptionAdded();
        event.userId = id;
        event.subscription = roleName;
        return event;
    }

    public SubscriptionRemoved subscriptionRemoved(String roleName) {
        SubscriptionRemoved event = new SubscriptionRemoved();
        event.userId = id;
        event.subscription = roleName;
        return event;
    }
}
