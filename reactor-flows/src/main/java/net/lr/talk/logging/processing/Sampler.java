package net.lr.talk.logging.processing;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import org.apache.karaf.rcomp.api.CloseableSubscriber;
import org.apache.karaf.rcomp.api.RComponent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;

import reactor.core.publisher.Flux;

@Component(immediate=true)
public class Sampler {
    
    @Reference(target="(name=eventAdmin)")
    RComponent eventAdmin;

	private Publisher<Map> from;
	private CloseableSubscriber<Map> to;

    @Activate
    public void start() {
        from = eventAdmin.from("net/lr/fast", Map.class);
        to = eventAdmin.to("decanter/collect/sampled", Map.class);
        Flux.from(from)
        		.sample(Duration.of(1, SECONDS))
             .subscribe(to);
    }
    
    @Deactivate
    public void stop() throws IOException {
        to.close();
    }
}
