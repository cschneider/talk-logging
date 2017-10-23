package net.lr.talk.logging.rest;

import static java.util.Collections.singletonList;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

@Component
public class ServiceStarter {

    private Server server;

    @Activate
    public void start() {
        server = create(new MyServiceImpl());
    }
    
    @Deactivate
    public void stop() {
        server.destroy();
    }

    private Server create(MyServiceImpl service) {
        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        Bus bus = BusFactory.newInstance().createBus();
        factory.setBus(bus);
        factory.setServiceClass(service.getClass());
        factory.setResourceProvider(service.getClass(), new SingletonResourceProvider(service));
        factory.setAddress("/echo");
        factory.setFeatures(singletonList(loggingFeature()));
        return factory.create();
    }

    private LoggingFeature loggingFeature() {
        LoggingFeature loggingFeature = new LoggingFeature();
        loggingFeature.setVerbose(false);
        return loggingFeature;
    }
}
