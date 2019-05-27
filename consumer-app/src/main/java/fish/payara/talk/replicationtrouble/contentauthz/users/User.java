package fish.payara.talk.replicationtrouble.contentauthz.users;

import fish.payara.talk.replicationtrouble.contentauthz.Subscription;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@NamedQuery(name = User.SUBSCRIPTION_AUTHORIZED,
        query = "select s.id from User u join u.subscriptions s where u.id =:userId and s.id = :subscriptionId")
@NamedQuery(name = User.CONTENT_AUTHORIZED,
        query = "select s.id from User u join u.subscriptions s join s.contents c where c = :contentId and u.id = :userId")
@Table(name = "CONTENT_USER")
public class User {
    static final String SUBSCRIPTION_AUTHORIZED = "User.authorized";
    static final String CONTENT_AUTHORIZED = "User.contentAuthorized";
    @Id
    String id;

    @ManyToMany
    Set<Subscription> subscriptions;

    protected User() {

    }

    User(String id) {
        this.id = id;
    }

    static boolean isSubscritionAuthorized(EntityManager em, String userId, String subscriptionId) {
        List<String> matchingSubcription = em.createNamedQuery(SUBSCRIPTION_AUTHORIZED, String.class)
                .setParameter("userId", userId)
                .setParameter("subscriptionId", subscriptionId)
                .getResultList();
        return !matchingSubcription.isEmpty();
    }

    static boolean isContentAuthorized(EntityManager em, String userId, String contentId) {
        List<String> matchingSubcription = em.createNamedQuery(CONTENT_AUTHORIZED, String.class)
                .setParameter("userId", userId)
                .setParameter("contentId", contentId)
                .getResultList();
        return !matchingSubcription.isEmpty();
    }
}
