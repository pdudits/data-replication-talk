package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import org.junit.Test;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.core.GenericType;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DeserializationTest {
    static final String eventJson = "{\"eventType\":\"UserCreated\",\"id\":1,\"payload\":{\"userId\":\"user1\"}}";
    @Test
    public void userCreated() {
        ReplicationEvent event = JsonbBuilder.create().fromJson(eventJson, ReplicationEvent.class);
        assertEquals(ReplicationEvent.EventType.UserCreated, event.eventType);
        assertEquals(1, event.id);
        assertEquals("user1", event.payload.userId);
    }

    @Test
    public void userCreatedArray() {
        String eventArray = "[" + eventJson + "," + eventJson + "]";
        List<ReplicationEvent> events = JsonbBuilder.create().fromJson(eventArray, new GenericType<List<ReplicationEvent>>(){}.getType());
        assertEquals(2, events.size());
    }
}
