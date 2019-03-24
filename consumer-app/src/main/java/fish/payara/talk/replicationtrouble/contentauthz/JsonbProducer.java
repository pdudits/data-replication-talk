package fish.payara.talk.replicationtrouble.contentauthz;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

@ApplicationScoped
public class JsonbProducer {
    @Produces
    @ApplicationScoped
    Jsonb jsonb() {
        return JsonbBuilder.create();
    }
}
