package net.lr.talk.logging.processing;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

@Component(immediate=true)
public class FastEmitter {
    
    @Reference
    EventAdmin eventAdmin;
    
	private boolean running;

    @Activate
    public void start() {
    		this.running = true;
    		ExecutorService executor = Executors.newSingleThreadExecutor();
    		executor.submit(new Runnable() {
				
				@Override
				public void run() {
					Random rand = new Random();
					while (running) {
						Map<String, Object> props = new HashMap<>();
						props.put("num", rand.nextInt(10000));
						eventAdmin.postEvent(new Event("net/lr/fast", props));
						try {
							TimeUnit.MILLISECONDS.sleep(10);
						} catch (InterruptedException e) {
						}
					}
				}
			});
    }
    
    @Deactivate
    public void stop() {
        this.running = false;
    }
}
