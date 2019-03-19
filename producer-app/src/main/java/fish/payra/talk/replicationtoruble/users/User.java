package fish.payra.talk.replicationtoruble.users;

import fish.payra.talk.replicationtoruble.users.events.RoleAdded;
import fish.payra.talk.replicationtoruble.users.events.RoleRemoved;
import fish.payra.talk.replicationtoruble.users.events.UserCreated;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Set;

@Entity
public class User {
    @Id
    String id;

    @Column
    String name;

    @ElementCollection
    Set<String> roles;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    UserCreated created() {
        UserCreated event = new UserCreated();
        event.userId = id;
        return event;
    }

    public RoleAdded roleAdded(String roleName) {
        RoleAdded event = new RoleAdded();
        event.userId = id;
        event.role = roleName;
        return event;
    }

    public RoleRemoved roleRemoved(String roleName) {
        RoleRemoved event = new RoleRemoved();
        event.userId = id;
        event.role = roleName;
        return event;
    }
}
