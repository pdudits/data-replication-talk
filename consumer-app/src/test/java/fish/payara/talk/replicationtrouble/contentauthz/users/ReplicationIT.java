package fish.payara.talk.replicationtrouble.contentauthz.users;

import fish.payara.talk.replicationtrouble.contentauthz.Subscription;
import fish.payara.talk.replicationtrouble.contentauthz.users.replication.UserDataPoller;
import fish.payara.talk.replicationtrouble.contentauthz.users.replication.UserDataScheduler;
import fish.payara.talk.replicationtrouble.contentauthz.users.replication.UserPollStub;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class ReplicationIT {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/consumer-app.war"))
                .deleteClass(UserDataScheduler.class);
        webArchive.delete("WEB-INF/classes/META-INF/microprofile-config.properties"); // duplicate file
        return webArchive
                .addClass(UserPollStub.class)
                .addAsManifestResource("replication-it.properties", "microprofile-config.properties")
                .addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml").importTestDependencies().resolve("org.assertj:assertj-core").withTransitivity().asFile());
    }

    @Inject
    UserDataPoller poller;

    @Inject
    Users users;

    @Test
    public void dataReplicated_andApplied() {
        poller.poll();
        User user = users.find("userRep1");
        assertNotNull(user);

        assertThat(user.subscriptions).extracting(Subscription::getId).contains("abc");
    }
}
