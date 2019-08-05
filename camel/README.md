Camel Routing Showcases
=======================

Overview
--------

The Camel routing showcases demonstrate how Camel can be used to integrate
Twitter, translation services, and ultimately, leverage AMQ asynchronous
messaging to integrate with other applications outside of the chosen runtime
environment.

Deployment Options
------------------

You can run this booster in the following modes:

- locally, in a standalone app
- locally, using SpringBoot runtime
- in OpenShift (any modality) simply using the ``oc new-app`` function

***IMPORTANT***: This application requires Java 8 JDK or later and Maven 3.3.x or later.

Running Locally as a Standalone App
-----------------------------------

Simply leverage the Maven ``exec`` plugin to do this:

    $ mvn clean compile
    $ mvn exec:java -Dexec.mainClass=com.redhat.training.agile.camel.Main

See sections below for more configuration information.

***IMPORTANT***: The Camel context will terminate after 10 seconds of idle time.

Running Locally as a SpringBoot App
-----------------------------------

Build the application using the ``package`` goal and execute the resulting JAR:

    $ mvn clean package -DskipTests
    $ java -jar target/agile-camel-1.0.0.jar

See sections below for more configuration information.

Running in OpenShift
--------------------

TBD.

