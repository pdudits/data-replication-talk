package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import fish.payara.talk.replicationtrouble.contentauthz.users.Users;

import javax.json.bind.Jsonb;
import java.util.function.BiConsumer;

public class ReplicationEvent {
    public long id;
    public EventType eventType;
    public GenericPayload payload;

    public ReplicationEvent() {

    }

    public ReplicationEvent(long id, String eventType, GenericPayload payload) {
        this.id = id;
        this.eventType = EventType.valueOf(eventType);
        this.payload = payload;
    }

    void apply(Cursor c, Jsonb jsonb, Users processor) {
        eventType.accept(processor, payload);
        c.lastId = id;
    }

    enum EventType {
        UserCreated((proc, p) -> proc.addUser(p.userId)),
        UserRemoved((proc, p) -> proc.removeUser(p.userId)),
        SubscriptionAdded((proc, p) -> proc.addSubscription(p.userId, p.subscription)),
        SubscriptionRemoved((proc, p) -> proc.removeSubscription(p.userId, p.subscription));
        ;

        private final BiConsumer<Users, GenericPayload> processMethod;

        EventType(BiConsumer<Users, GenericPayload> processMethod) {
            this.processMethod = processMethod;
        }

        public void accept(Users users, GenericPayload genericPayload) {
            processMethod.accept(users, genericPayload);
        }
    }

    public static class GenericPayload {
        public String userId;
        public String subscription;

        public GenericPayload() {

        }

        public GenericPayload(String userId, String subscription) {
            this.userId = userId;
            this.subscription = subscription;
        }
    }
}
