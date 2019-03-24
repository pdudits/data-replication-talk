package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.function.BiConsumer;

//TODO: Automatic Registration doesn't work
//@RegisterRestClient
@Path("/")
public interface ReplicationAPI {
    @GET
    List<ReplicationEvent> fetch(@QueryParam("id") Long lastSeenId,
                                 @QueryParam("size") int pageSize);

}
