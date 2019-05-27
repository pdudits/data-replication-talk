package fish.payra.talk.replicationtoruble.users;

import fish.payra.talk.replicationtoruble.users.events.SubscriptionAdded;
import fish.payra.talk.replicationtoruble.users.events.SubscriptionRemoved;
import fish.payra.talk.replicationtoruble.users.events.UserCreated;
import fish.payra.talk.replicationtoruble.users.events.UserEvent;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashSet;
import java.util.UUID;

@Path("/user")
@RequestScoped
public class UserResource {
    @PersistenceContext
    EntityManager mgr;

    @Context UriInfo info;

    @Inject
    Event<UserEvent> event;

    @POST
    @Path("/{name}")
    @Transactional
    public Response createUser(@PathParam("name") String name) {
        User u = new User();
        u.name = name;
        u.id = UUID.randomUUID().toString();
        mgr.persist(u);
        event.fire(u.created());
        return Response.created(userUri(u)).build();
    }

    private URI userUri(User u) {
        return info.getBaseUriBuilder().path(UserResource.class).path(u.id).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id) {
        User u = mgr.find(User.class, id);
        if (u != null) {
            return Response.ok(u).build();
        } else {
            return notFound();
        }
    }

    @PUT
    @Path("/{id}/subscriptions/{name}")
    @Transactional
    public Response addSubscription(@PathParam("id") String id, @PathParam("name") String subscriptionName) {
        User u = mgr.find(User.class, id);
        if (u != null) {
            if (u.subscriptions == null) {
                u.subscriptions = new HashSet<>();
            }
            if (u.subscriptions.add(subscriptionName)) {
                event.fire(u.subscriptionAdded(subscriptionName));
                return Response.created(userUri(u)).entity(u.subscriptions).build();
            } else {
                return Response.notModified().build();
            }
        } else {
            return notFound();
        }
    }

    @DELETE
    @Path("/{id}/subscriptions/{name}")
    @Transactional
    public Response removeSubscription(@PathParam("id") String id, @PathParam("name") String subscriptionName) {
        User u = mgr.find(User.class, id);
        if (u != null) {
            if (u.subscriptions == null) {
                u.subscriptions = new HashSet<>();
            }
            if (u.subscriptions.remove(subscriptionName)) {
                event.fire(u.subscriptionRemoved(subscriptionName));
                return Response.ok(userUri(u)).entity(u.subscriptions).build();
            } else {
                return Response.notModified().build();
            }
        } else {
            return notFound();
        }
    }

    private Response notFound() {
        return Response.status(Response.Status.NOT_FOUND).entity("Not found, sorry").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

    @GET
    public JsonArray getUsers() {
        // just ad hoc projection of just IDs
        JsonArrayBuilder result = Json.createArrayBuilder();
        mgr.createQuery("select u from User u order by u.name", User.class)
                .getResultStream().forEach(u -> result.add(Json.createObjectBuilder().add("id", u.id)));
        return result.build();
    }


}
