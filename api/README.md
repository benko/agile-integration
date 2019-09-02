REST API Showcase
=================

Overview
--------

This simple Thorntail app is a demo of how easy it is to develop
integration applications that expose backend JMS endpoints through
REST APIs.

Deployment Options
------------------

You can run this application in the following modes:

- locally, using Thorntail runtime
- in OpenShift (any modality) simply using the ``oc new-app`` function

***IMPORTANT***: This application requires Java 8 JDK or later and Maven 3.3.x or later.

Running Locally as a Thorntail App
----------------------------------

Build the application using the ``package`` goal and execute the resulting JAR:

    $ mvn clean package
    $ java -jar ./target/agile-api-1.0.0-thorntail.jar -s./src/main/resources/project-local.yml

See sections below for more configuration information.

Running in OpenShift
--------------------

TBD.

Configuring Application Parameters
----------------------------------

TBD.
