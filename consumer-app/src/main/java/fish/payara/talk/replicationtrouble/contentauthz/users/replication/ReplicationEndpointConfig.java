package fish.payara.talk.replicationtrouble.contentauthz.users.replication;

import fish.payara.micro.PayaraMicroRuntime;
import fish.payara.micro.data.InstanceDescriptor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.net.URI;

@Dependent
class ReplicationEndpointConfig {

    @Inject
    @ConfigProperty(name = "fish.payara.talk.replicationtrouble.contentauthz.user.replication.ReplicationAPI/mp-rest/url")
    String endpointUrl;

    @Inject
    PayaraMicroRuntime runtime;

    @Inject
    ServletContext servletContext;

    @Produces
    @RestClient
    ReplicationAPI replicationEndpoint() {
        return RestClientBuilder.newBuilder().baseUri(determineUri()).build(ReplicationAPI.class);
    }

    private URI determineUri() {
        if (endpointUrl == null) {
            throw new IllegalArgumentException("Base URI for endpoint is not defined");
        } else if (endpointUrl.startsWith("./")) {
            InstanceDescriptor descr = runtime.getLocalDescriptor();
            Integer port = descr.getHttpPorts().get(0);
            return URI.create(String.format("http://localhost:%d%s%s",port,servletContext.getContextPath(),endpointUrl.substring(1)));
        } else {
            return URI.create(endpointUrl);
        }
    }
}
