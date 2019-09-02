Camel Routing Showcase
======================

Overview
--------

The Camel routing showcase demonstrates how Camel can be used to integrate
Twitter, translation services, and ultimately, leverage AMQ asynchronous
messaging to integrate with other applications outside of the chosen runtime
environment.

Deployment Options
------------------

You can run this application in the following modes:

- locally, using SpringBoot runtime
- in OpenShift (any modality) simply using the ``oc new-app`` function

***IMPORTANT***: This application requires Java 8 JDK or later and Maven 3.3.x or later.

External Dependencies
---------------------

This application depends on a number of external resources to do its job:

 - a configured Twitter application account (and credentials)
 - an active Systran application account (and credentials)
 - a running JMS broker for both internal communication (forwarding tweets
   to translation routes) and handoff to external applications (such as the
   REST query API)

See below for further configuration information.

Running Locally as a SpringBoot App
-----------------------------------

Build the application using the ``package`` goal and execute the resulting JAR:

    $ mvn clean package
    $ java -jar ./target/agile-camel-1.0.0.jar

See sections below for more configuration information.

Running in OpenShift
--------------------

TBD.

Configuring Application Parameters
----------------------------------

TBD.

References
----------

