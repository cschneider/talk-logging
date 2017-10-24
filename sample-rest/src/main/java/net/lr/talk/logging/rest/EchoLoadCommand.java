package net.lr.talk.logging.rest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component//
(//
    immediate=true,
    service= EchoLoadCommand.class,
    property = //
    {
     "osgi.command.scope=loadtest", //
     "osgi.command.function=start",
     "osgi.command.function=stop"
    }//
)
public class EchoLoadCommand {
    

    private ScheduledExecutorService executor;
    private ScheduledFuture<?> scheduleAtFixedRate;

    public EchoLoadCommand() {
        executor = Executors.newScheduledThreadPool(1);
    }

    public void start(Integer percentFail) {
    		stop();
        scheduleAtFixedRate = executor.scheduleAtFixedRate(new CallService(percentFail), 0, 200, TimeUnit.MILLISECONDS);
    }
    
    @Deactivate
    public void stop() {
        if (scheduleAtFixedRate != null) {
            scheduleAtFixedRate.cancel(false);
        }
    }
}
