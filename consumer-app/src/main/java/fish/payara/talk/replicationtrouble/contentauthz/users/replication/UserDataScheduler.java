package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.inject.Inject;

@Singleton
public class UserDataScheduler {
    @Inject
    UserDataPoller poller;

    @Schedule(hour = "*", minute = "*", second = "*/15")
    @Timeout
    void schedule() {
        poller.poll();
    }
}
