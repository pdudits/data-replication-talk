package fish.payra.talk.replicationtoruble.users.replication;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@RequestScoped
@Path("/replication")
public class ReplicationResource {

    @PersistenceContext
    EntityManager mgr;

    @GET
    public List<ReplicationEvent> fetch(@QueryParam("id") @DefaultValue("-1") long lastSeenId,
                                        @QueryParam("size") @DefaultValue("100") int pageSize) {
        return ReplicationEvent.fetchPage(mgr, lastSeenId, pageSize).getResultList();
    }

}