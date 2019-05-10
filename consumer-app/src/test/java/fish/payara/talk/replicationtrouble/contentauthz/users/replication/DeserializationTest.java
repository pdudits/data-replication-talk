package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import org.junit.Test;

import javax.json.bind.JsonbBuilder;

import static org.junit.Assert.assertEquals;

public class DeserializationTest {
    @Test
    public void userCreated() {
        String eventJson = "{\"eventType\":\"UserCreated\",\"id\":1,\"payload\":{\"userId\":\"user1\"}}";
        ReplicationEvent event = JsonbBuilder.create().fromJson(eventJson, ReplicationEvent.class);
        assertEquals(ReplicationEvent.EventType.UserCreated, event.eventType);
        assertEquals(1, event.id);
        assertEquals("user1", event.payload.userId);
    }
}
