From logging to monitoring to reactive insights
-----------------------------------------------

Demo project for the talk.

Setup Karaf with Decanter, Elasticsearch and Kibana
----------------------------------------------------

* Download and Unpack Apache Karaf 4.1.2

```
bin/karaf
```


Install Elastichsearch with Kibana and Decanter
```
feature:repo-add decanter 1.4.0
feature:install elasticsearch kibana
config:property-set -p org.ops4j.pax.web org.osgi.service.http.port 8182

```

In a second karaf install
```
feature:repo-add decanter 1.4.0
feature:install elasticsearch
feature:install decanter-collector-log decanter-collector-jmx decanter-collector-eventadmin decanter-appender-elasticsearch-rest
feature:repo-add mvn:org.apache.karaf.rcomp/rcomp-features/1.0.0-SNAPSHOT/xml/features
feature:install rcomp-eventadmin reactor
feature:repo-add cxf 3.2.0
feature:install cxf-jaxrs cxf-features-logging
install -s  mvn:net.lr.talk.logging/reactor-flows/1.0.0-SNAPSHOT
install -s  mvn:net.lr.talk.logging/sample-rest/1.0.0-SNAPSHOT
```

This installs elasticsearch and kibana into karaf. This is great for testing but on production these should
be deployed separately. We install the decanter log and jmx collectors and the elasticsearch appender. This is enough to get started.

Check config
------------

The default configurations are fine but we should take a look to understand what is going on.

etc/org.apache.karaf.decanter.collector.log.cfg
etc/org.apache.karaf.decanter.collector.jmx-local.cfg
etc/org.apache.karaf.decanter.appender.elasticsearch.rest.cfg

Open Kibana
------------

[Open Kibana in browser](http://localhost:8181/kibana) and log in with karaf/karaf.
If there is not yet a suitable index config then go to settings and set the index to karaf-*.

Check the log and system dashboards
-----------------------------------



Look for some logs
------------------

In Discovery search for type:log.
You should now see all log messages.
Add some fields MDC.bundle_name, level, message

Look for some jmx beans
-----------------------



