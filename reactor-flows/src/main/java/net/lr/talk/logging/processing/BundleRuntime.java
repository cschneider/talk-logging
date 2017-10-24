package net.lr.talk.logging.processing;

import java.util.HashMap;
import java.util.Map;

import org.apache.karaf.rcomp.api.RComponent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import reactor.core.publisher.Flux;

@Component(immediate=true)
@SuppressWarnings("rawtypes")
public class BundleRuntime {
    
    @Reference(target="(name=eventAdmin)")
    RComponent eventAdmin;
    
    Map<String, Long> startedTimePerBundle = new HashMap<>();
    
    private Subscriber<Map> to;

    private Publisher<Map> from;

    @Activate
    public void start() {
        from = eventAdmin.from("org/osgi/framework/BundleEvent/*", Map.class);
        to = eventAdmin.to("decanter/collect/bundle_runtime", Map.class);
        Flux.from(from)
                .map(event -> computeRunningTime(event))
                .filter(event -> !event.isEmpty())
                .subscribe(to);
    }

    private Map<String, Object> computeRunningTime(Map event) {
        String symbolicName = (String) event.get("bundle.symbolicName");
        String topic = (String) event.get("topic");
        long time = (long) event.get("timestamp");
        if (topic.endsWith("STARTED")) {
            startedTimePerBundle.put(symbolicName, time);
        }
        if (topic.endsWith("STOPPED")) {
            
            Long startedTime = startedTimePerBundle.remove(symbolicName);
            //System.out.println(symbolicName + " "+ topic + " " + startedTime);
            if (startedTime != null) {
                return createEvent(symbolicName, (time - startedTime));
            }
        }
        return new HashMap<>();
    }

    private Map<String, Object> createEvent(String symbolicName, long time) {
        HashMap<String, Object> event = new HashMap<>();
        event.put("bundle.symbolicName", symbolicName);
        event.put("time.running", time);
        return event;
    }
    private void printEvent(Map<String, Object> map) {
        System.out.println();
        for (String key : map.keySet()) {
            System.out.println(key + "=" + map.get(key));
        }
        
    }
    
    @Deactivate
    public void stop() {
        to.onComplete();
    }
}
