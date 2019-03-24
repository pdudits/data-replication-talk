package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import fish.payara.talk.replicationtrouble.contentauthz.users.replication.ReplicationAPI;
import fish.payara.talk.replicationtrouble.contentauthz.users.replication.ReplicationEvent;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/userpollstub")
public class UserPollStub implements ReplicationAPI {
    static ReplicationEvent[] events = {
            new ReplicationEvent(1, "UserCreated", "{\"userId\":\"userRep1\"}"),
            new ReplicationEvent(2, "SubscriptionAdded", "{\"userId\":\"userRep1\", \"subscription\":\"abc\"}")
    };



    @Override
    public List<ReplicationEvent> fetch(Long lastSeenId, int pageSize) {
        return Stream.of(events).filter(e -> lastSeenId == null || e.id > lastSeenId).limit(pageSize).collect(Collectors.toList());
    }
}
