package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/userpollstub")
public class UserPollStub implements ReplicationAPI {
    static ReplicationEvent[] events = {
            new ReplicationEvent(1, "UserCreated", new ReplicationEvent.GenericPayload("userRep1",null)),
            new ReplicationEvent(2, "SubscriptionAdded", new ReplicationEvent.GenericPayload("userRep1", "abc")),
    };

    @Override
    @GET
    public List<ReplicationEvent> fetch(@QueryParam("id") Long lastSeenId,  @QueryParam("size")  int pageSize) {
        return Stream.of(events).filter(e -> lastSeenId == null || e.id > lastSeenId).limit(pageSize).collect(Collectors.toList());
    }
}
