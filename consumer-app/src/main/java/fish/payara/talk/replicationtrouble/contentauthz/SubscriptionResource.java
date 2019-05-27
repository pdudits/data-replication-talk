package fish.payara.talk.replicationtrouble.contentauthz;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/subscription")
@RequestScoped
public class SubscriptionResource {
    @PersistenceContext
    EntityManager mgr;

    @GET
    public JsonArray listSubscriptions() {
        JsonArrayBuilder result = Json.createArrayBuilder();
        mgr.createQuery("select s from Subscription s order by s.id", Subscription.class)
                .getResultStream()
                .map(s -> Json.createObjectBuilder().add("id", s.id))
                .forEach(result::add);
        return result.build();
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public Subscription createSubscription(@PathParam("id") String id) {
        Subscription s = mgr.find(Subscription.class, id);
        if (s != null) {
            throw new WebApplicationException(Response.Status.NOT_MODIFIED);
        } else {
            s = new Subscription();
            s.id = id;
            mgr.persist(s);
            return s;
        }
    }

    @Path("/{id}")
    @GET
    public Subscription getSubscription(@PathParam("id") String id) {
        Subscription s = mgr.find(Subscription.class, id);
        if (s != null) {
            return s;
        } else {
            throw new NotFoundException();
        }
    }

    @Path("/{id}/{contentId}")
    @PUT
    @Transactional
    public Subscription addContent(@PathParam("id") String id, @PathParam("contentId") String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new BadRequestException();
        }
        Subscription s = getSubscription(id);
        if (s.addContent(contentId)) {
            return s;
        } else {
            throw new WebApplicationException(Response.Status.NOT_MODIFIED);
        }
    }

    @Path("/{id}/{contentId}")
    @DELETE
    @Transactional
    public Subscription removeContent(@PathParam("id") String id, @PathParam("contentId") String contentId) {
        if (contentId == null || contentId.trim().isEmpty()) {
            throw new BadRequestException();
        }
        Subscription s = getSubscription(id);
        if (s.removeContent(contentId)) {
            return s;
        } else {
            throw new WebApplicationException(Response.Status.NOT_MODIFIED);
        }
    }
}
