/*
 *    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 *    Copyright (c) [2019] Payara Foundation and/or its affiliates. All rights reserved.
 *
 *    The contents of this file are subject to the terms of either the GNU
 *    General Public License Version 2 only ("GPL") or the Common Development
 *    and Distribution License("CDDL") (collectively, the "License").  You
 *    may not use this file except in compliance with the License.  You can
 *    obtain a copy of the License at
 *    https://github.com/payara/Payara/blob/master/LICENSE.txt
 *    See the License for the specific
 *    language governing permissions and limitations under the License.
 *
 *    When distributing the software, include this License Header Notice in each
 *    file and include the License file at glassfish/legal/LICENSE.txt.
 *
 *    GPL Classpath Exception:
 *    The Payara Foundation designates this particular file as subject to the "Classpath"
 *    exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 *    file that accompanied this code.
 *
 *    Modifications:
 *    If applicable, add the following below the License Header, with the fields
 *    enclosed by brackets [] replaced by your own identifying information:
 *    "Portions Copyright [year] [name of copyright owner]"
 *
 *    Contributor(s):
 *    If you wish your version of this file to be governed by only the CDDL or
 *    only the GPL Version 2, indicate your decision by adding "[Contributor]
 *    elects to include this software in this distribution under the [CDDL or GPL
 *    Version 2] license."  If you don't indicate a single choice of license, a
 *    recipient has the option to distribute your version of this file under
 *    either the CDDL, the GPL Version 2 or to extend the choice of license to
 *    its licensees as provided above.  However, if you add GPL Version 2 code
 *    and therefore, elected the GPL Version 2 license, then the option applies
 *    only if the new code is made subject to such option by the copyright
 *    holder.
 */

package fish.payara.talk.replicationtrouble.users.replication;

import fish.payra.talk.replicationtoruble.users.replication.ReplicationEvent;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class EventStorageIT {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/producer-app.war"))
                .addClass(ReplicationEventJson.class)
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").importTestDependencies().resolve("org.assertj:assertj-core").withTransitivity().asFile());
    }

    @ArquillianResource
    URI baseUrl;

    @Test
    public void events_created_as_result_of_operations() {
        Client client = ClientBuilder.newClient();
        WebTarget userResource = client.target(baseUrl).path("user/");
        String userName = "user1";

        createUser(client, userResource, userName);

        WebTarget replicationResource = client.target(baseUrl).path("replication");
        List<ReplicationEventJson> events = replicationResource.request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<ReplicationEventJson>>() {});
        System.out.println(events);

        assertThat(events)
                .hasSize(5)
                .extracting(ev -> ev.eventType)
                .contains("UserCreated",
                        "SubscriptionAdded","SubscriptionAdded","SubscriptionAdded",
                        "SubscriptionRemoved");
    }

    private URI createUser(Client client, WebTarget userResource, String userName) {
        Entity<String> empty = Entity.json("");
        Response response;
        response = userResource.path(userName).request().post(empty);

        assertEquals(Response.Status.CREATED, response.getStatusInfo().toEnum());
        assertNotNull(response.getLocation());
        URI user1 = response.getLocation();
        WebTarget user1subs = client.target(user1).path("subscriptions/{name}");
        response = user1subs.resolveTemplate("name", "sub1").request().put(empty);
        assertEquals(SUCCESSFUL, response.getStatusInfo().getFamily());
        response = user1subs.resolveTemplate("name", "sub2").request().put(empty);
        assertEquals(SUCCESSFUL, response.getStatusInfo().getFamily());
        response = user1subs.resolveTemplate("name", "sub3").request().put(empty);
        assertEquals(SUCCESSFUL, response.getStatusInfo().getFamily());
        response = user1subs.resolveTemplate("name", "sub2").request().delete();
        assertEquals(SUCCESSFUL, response.getStatusInfo().getFamily());
        return user1;
    }
}
