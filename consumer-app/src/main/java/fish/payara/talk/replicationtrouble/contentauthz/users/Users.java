package fish.payara.talk.replicationtrouble.contentauthz.users;

import fish.payara.talk.replicationtrouble.contentauthz.Subscription;
import fish.payara.talk.replicationtrouble.contentauthz.Subscriptions;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;

@Transactional
@RequestScoped
public class Users {
    @Inject
    Subscriptions subscriptions;

    @PersistenceContext
    EntityManager em;

    public void addUser(String userId) {
        em.persist(new User(userId));
    }

    public void removeUser(String userId) {
        em.remove(em.find(User.class, userId));
    }

    public void addSubscription(String userId, String subscriptionId) {
        Subscription subscription = subscriptions.find(subscriptionId);

        User user = find(userId);
        if (user.subscriptions == null) {
            user.subscriptions = new HashSet<>();
        }
        user.subscriptions.add(subscription);
    }

    public User find(String userId) {
        return em.find(User.class, userId);
    }

    public void removeSubscription(String userId, String subscriptionId) {
        Subscription subscription = em.find(Subscription.class, subscriptionId);
        User user = find(userId);
        user.subscriptions.remove(subscription);
    }

    public boolean isContentAuthorized(String contentId, String userId) {
        return User.isContentAuthorized(em, userId, contentId);
    }
}
