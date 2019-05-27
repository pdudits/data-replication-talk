package fish.payara.talk.replicationtrouble.contentauthz;

import fish.payara.talk.replicationtrouble.contentauthz.users.User;
import fish.payara.talk.replicationtrouble.contentauthz.users.Users;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/content")
@RequestScoped
public class ContentResource {
    @Inject
    Users users;

    @GET
    @Path("/{id}")
    public String getContent(@PathParam("id") String contentId, @HeaderParam("Authorization") String userId) {
        if (users.isContentAuthorized(contentId, userId)) {
            return "{}";
        } else {
            throw new ForbiddenException(userId + " cannot access");
        }
    }
}
