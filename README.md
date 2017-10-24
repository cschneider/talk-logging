# From logging to monitoring to reactive insights

Demo project for the talk.

## Setup Elasticsearch and Kibana

Download and start elastic search and kibana.
Use the default settings.

## Setup Apache Karaf and decanter

* Download and Unpack Apache Karaf 4.1.2

```
bin/karaf
```

```
feature:repo-add decanter 1.4.0
feature:install decanter-collector-log decanter-collector-jmx decanter-collector-eventadmin decanter-appender-elasticsearch-rest
feature:repo-add mvn:org.apache.karaf.rcomp/rcomp-features/1.0.0-SNAPSHOT/xml/features
feature:install rcomp-eventadmin reactor
feature:repo-add cxf 3.2.0
feature:install cxf-jaxrs cxf-features-logging
install -s  mvn:net.lr.talk.logging/reactor-flows/1.0.0-SNAPSHOT
install -s  mvn:net.lr.talk.logging/sample-rest/1.0.0-SNAPSHOT
```

This installs some decanter collectors and the elastic search appender.
We also install reactive components and a CXF Rest example to show the message logging and 
some possibilities for extensions.

## Check config

The default configurations are fine but we should take a look to understand what is going on.

etc/org.apache.karaf.decanter.collector.log.cfg
etc/org.apache.karaf.decanter.collector.jmx-local.cfg
etc/org.apache.karaf.decanter.appender.elasticsearch.rest.cfg

## Open Kibana

[Open Kibana in browser](http://localhost:5601/).
For the first time configure the index in Management/Index Patterns and set the index to karaf-*.
You might need to refresh this index later whenever you get new fields.

## Create some searches and visualizations 


### Create these searches

* LOG
    _type=log
* CXF Messages
    _type=log loggerName:org.apache.cxf.services.*
* Operating System JMX Bean
    ObjectName:"java.lang:type=OperatingSystem"
    
Base on the searches create suitable visualizations and dashboards

### Dashboards

You can create these Dashboards to get a good overview of what can be monitored

* OS: Visualization for Operting Systzem JMX bean with P
* Log: Visualzations for level.KEYWORD and loggername.KEYWORD and table with log details
* CXF Messages: Visualization for MDC.ResponseCode.keyword and table with MDC.type, MDC.slf4j_marker, MDC.exchangeId, MDC.address, message

### Check reactive processes

The FastEmitter produces elements too fast
event:tail net/lr/fast

The Sampler makes sure we get only one sample per second
event:tail decanter/collect/sampled

In Kibana
event_topics:"decanter/collect/sampled"


