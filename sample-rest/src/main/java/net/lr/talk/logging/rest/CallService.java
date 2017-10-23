package net.lr.talk.logging.rest;

import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;

public class CallService implements Runnable {

    private Client client;
    private Integer percentFail;
    private Random random;
    
    public CallService(Integer percentFail) {
        this.percentFail = percentFail;
        client = ClientBuilder.newClient();
        random = new Random();
    }

    @Override
    public void run() {
        if (random.nextInt(100) < percentFail) {
            call("coffee");
        } else {
            call("Hello");
        }
    }

    private void call(String message) {
        Builder req = client.target("http://localhost:8181/cxf/echo").path(message).request();
        req.get();
    }
    
}
