package fish.payara.talk.replicationtrouble.contentauthz.users;

import fish.payara.talk.replicationtrouble.contentauthz.Subscription;
import fish.payara.talk.replicationtrouble.contentauthz.Subscriptions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class UserAuthorizationIT {
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(Subscription.class.getPackage())
                .addPackage(Users.class.getPackage())
                .addClass(DataFixture.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @Inject
    Subscriptions subs;

    @Inject
    Users users;


    @Test
    public void singleSub_found() {
        assertTrue("User should be authorized for content 'a'", users.isContentAuthorized("a", "user1"));
        assertTrue("User should be authorized for content 'b'", users.isContentAuthorized("b", "user1"));
    }

    @Test
    public void singleSub_notFound() {
        assertFalse("User should not be authorized form content 'e'", users.isContentAuthorized("e", "user1"));
    }

    @Test
    public void nonExistingContent_notFound() {
        assertFalse("Non existing content returns false", users.isContentAuthorized("x", "user1"));
    }

    @Test
    public void multiSub_found() {
        assertTrue("User should be authorized for both subs", users.isContentAuthorized("a", "user2"));
        assertTrue("User should be authorized for both subs", users.isContentAuthorized("e", "user2"));
    }

}
