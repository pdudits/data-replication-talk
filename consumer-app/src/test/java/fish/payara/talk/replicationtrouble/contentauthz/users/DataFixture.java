package fish.payara.talk.replicationtrouble.contentauthz.users;

import fish.payara.talk.replicationtrouble.contentauthz.Subscription;
import fish.payara.talk.replicationtrouble.contentauthz.Subscriptions;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataFixture {
    @Inject
    Subscriptions subs;

    @Inject
    Users users;

    @PostConstruct
    void initData() {
        System.out.println("Preparing data");
        Subscription subscription1 = subs.addSubscriptionContent("abcd", "a", "b", "c", "d");
        Subscription subscription2 = subs.addSubscriptionContent("efgh", "e", "f", "g", "h");
        users.addUser("user1");
        users.addSubscription("user1", "abcd");

        users.addUser("user2");
        users.addSubscription("user2", "abcd");
        users.addSubscription("user2", "efgh");
    }

    void startup(@Observes @Initialized(ApplicationScoped.class) Object init) {
        // this causes postconstruct to be invoked. Somehow doing this directly is too soon, and request scoped
        // beans cannot be reached
    }
}
