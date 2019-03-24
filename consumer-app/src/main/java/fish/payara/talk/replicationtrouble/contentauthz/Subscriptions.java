package fish.payara.talk.replicationtrouble.contentauthz;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.stream.Stream;

@Transactional
@RequestScoped
public class Subscriptions {
    @PersistenceContext
    EntityManager em;

    public Subscription find(String subscriptionId) {
        Subscription subscription = em.find(Subscription.class, subscriptionId);
        if (subscription == null) {
            subscription = new Subscription(subscriptionId);
            em.persist(subscription);
        }
        return subscription;
    }

    public Subscription addSubscriptionContent(String subscriptionId, String... contentIds) {
        Subscription subscription = find(subscriptionId);
        Stream.of(contentIds).forEach(subscription::addContent);
        return subscription;
    }
}
