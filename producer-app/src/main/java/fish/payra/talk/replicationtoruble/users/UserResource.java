package fish.payra.talk.replicationtoruble.users;

import fish.payra.talk.replicationtoruble.users.events.RoleAdded;
import fish.payra.talk.replicationtoruble.users.events.RoleRemoved;
import fish.payra.talk.replicationtoruble.users.events.UserCreated;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.management.relation.Role;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
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
    Event<UserCreated> userCreated;

    @Inject
    Event<RoleAdded> roleAdded;

    @Inject
    Event<RoleRemoved> roleRemoved;

    @POST
    @Path("/{name}")
    @Transactional
    public Response createUser(@PathParam("name") String name) {
        User u = new User();
        u.name = name;
        u.id = UUID.randomUUID().toString();
        userCreated.fire(u.created());
        mgr.persist(u);
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
    @Path("/{id}/roles/{name}")
    @Transactional
    public Response addRole(@PathParam("id") String id, @PathParam("name") String roleName) {
        User u = mgr.find(User.class, id);
        if (u != null) {
            if (u.roles == null) {
                u.roles = new HashSet<>();
            }
            if (u.roles.add(roleName)) {
                roleAdded.fire(u.roleAdded(roleName));
                return Response.created(userUri(u)).entity(u.roles).build();
            } else {
                return Response.notModified().build();
            }
        } else {
            return notFound();
        }
    }

    @DELETE
    @Path("/{id}/roles/{name}")
    @Transactional
    public Response removeRole(@PathParam("id") String id, @PathParam("name") String roleName) {
        User u = mgr.find(User.class, id);
        if (u != null) {
            if (u.roles == null) {
                u.roles = new HashSet<>();
            }
            if (u.roles.remove(roleName)) {
                roleRemoved.fire(u.roleRemoved(roleName));
                return Response.created(userUri(u)).entity(u.roles).build();
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


}
