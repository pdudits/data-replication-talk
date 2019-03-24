package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import fish.payara.talk.replicationtrouble.contentauthz.users.Users;

import javax.json.bind.Jsonb;
import java.util.function.BiConsumer;

public class ReplicationEvent {
    public long id;
    public String eventType;
    public String payload;

    public ReplicationEvent() {

    }

    public ReplicationEvent(long id, String eventType, String payload) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
    }

    void apply(Cursor c, Jsonb jsonb, Users processor) {
        EventType event = EventType.valueOf(eventType);
        GenericPayload parsedPayload = jsonb.fromJson(payload, GenericPayload.class);

        event.accept(processor, parsedPayload);
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
    }
}
