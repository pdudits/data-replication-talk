package fish.payara.talk.replicationtrouble.users.replication;

import javax.json.JsonObject;

public class ReplicationEventJson {
    public long id;

    public String eventType;

    public JsonObject payload;

    @Override
    public String toString() {
        return "ReplicationEvent{" +
                "\n    id: " + id +
                ",\n    eventType: \"" + eventType + '\"' +
                ",\n    payload: " + payload +
                "\n}\n";
    }
}
