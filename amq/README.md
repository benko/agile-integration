JMS API Showcase
================

Overview
--------

This is just a collection of simple JMS clients.

The important asset in this project is the example ``broker.xml``
configuration which contains the destination definitions used by the
other two projects, and the links (diverts) between the two. 

Deployment Options
------------------

You can only run this application locally, by deploying and starting an
AMQ broker first.

External Dependencies
---------------------

This application depends on having an AMQ broker up and running prior to
running the clients.

A simple way of deploying a broker would be by using the ``artemis create``
command (you can remove backslashes and write everything on a single line):

    $ /opt/amq-7.4/bin/artemis create \
        --name agile-broker \
        --allow-anonymous \
        --user admin \
        --password admin \
        -- \
        ./broker

After that, simply replace the ``broker.xml`` file with one from this project
and you're all set to run the broker:

    $ ./broker/bin/artemis run

Optionally, have a look at the ``logging.properties`` for any possible tweaks.

Running the Sample Clients
--------------------------

Build the application using the ``compile`` goal and then execute the
``exec:java`` goal using one of the available main classes (see full list
below):

    $ mvn clean compile
    $ mvn exec:java -Dexec.mainClass=<className>

The following classes are available:

 - ``com.redhat.training.agile.amq.QueueProducer``
 - ``com.redhat.training.agile.amq.QueueConsumer``
 - ``com.redhat.training.agile.amq.GenericConsumer``

Configuring Application Parameters
----------------------------------

For ``GenericConsumer``, there are three recognised system properties:

 - ``consumer.queueName`` is the JMS destination to read from; must be a queue defined in ``jndi.properties``
 - ``consumer.delay`` is the amount of time to wait for a message to appear when receiving
 - ``consumer.attempts`` is the number of receive attempts before giving up (receiving one message only)

Simply set them on Maven command line (or otherwise).

References
----------

 - [ActiveMQ Artemis: Using JMS](https://activemq.apache.org/components/artemis/documentation/latest/using-jms.html)
 - [ActiveMQ Artemis: Last-Value Queues](https://activemq.apache.org/components/artemis/documentation/latest/last-value-queues.html)
 - [ActiveMQ Artemis: Diverting and Splitting Flows](https://activemq.apache.org/components/artemis/documentation/latest/diverts.html)
 - [ActiveMQ Artemis: Configuration Reference](https://activemq.apache.org/components/artemis/documentation/latest/configuration-index.html)
