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

External Dependencies
---------------------

The application requires a running JMS broker (endpoints configured in the
project staging file) with a couple of destinations to read the tweets from. 

Configuring Application Parameters
----------------------------------

TBD.

Running Locally as a Thorntail App
----------------------------------

Build the application using the ``package`` goal and execute the resulting JAR:

    $ mvn clean package
    $ java -jar ./target/agile-api-1.0.0-thorntail.jar

See sections below for more configuration information.

Running in OpenShift
--------------------

Caveat: you will need to have the ``agile-model`` JAR file available before
the build of this application will succeed. If there is a Nexus instance
available somewhere in the cluster, and it allows you to upload artifacts,
you could approach that in a way similar to this:

    $ oc new-build https://github.com/benko/agile-integration.git \
            --name=twitter-model \
            --context-dir=model \
            --strategy=source \
            --no-output \
            -i redhat-openjdk18-openshift:1.5 \
            --build-env=MAVEN_MIRROR_URL=http://nexus.apps.mycloud.com/java/ \
            --build-env=MAVEN_ARGS="-e -DskipTests -Dcom.redhat.xpaas.repo.redhatga deploy" \
            --build-env=MAVEN_ARGS_APPEND="-DaltDeploymentRepository=nexus::default::http://nexus.apps.mycloud.com/java/"
    ...
    $ oc delete is twitter-model
    ...

Alternatively, you could configure the main build to be a multi-module one,
making sure you set ``ARTIFACT_DIR``, adding the ``-pl`` option to ``MAVEN_ARGS``,
and switching the default goal to ``install`` rather than ``package``:

    $ oc new-app ... \
            --build-env=ARTIFACT_DIR=api/target \
            --build-env=MAVEN_ARGS="-e -DskipTests -Dcom.redhat.xpaas.repo.redhatga -pl model,api install"

You will also need an AMQ broker deployment prior to successfully running
this application. Consult the top-level documentation and the ``amq`` project
for more on this.

Creating the application should be as simple as telling OpenShift to build a
project; do not forget the staging file however, use ``JAVA_ARGS`` for that:

    $ oc new-app https://github.com/benko/agile-integration.git \
            --name=twitter-api \
            --context-dir=api \
            --strategy=source \
            -i redhat-openjdk18-openshift:1.5 \
            --build-env=MAVEN_MIRROR_URL=http://nexus.apps.mycloud.com/java/
    ...

TODO: STAGING FILE & ENV SETTINGS!

    $ oc set env dc/twitter-api AMQ_BROKER_HOSTNAME=broker-amq-tcp
    ...

For Thorntail, default metaspace size is too small.

    $ oc set env dc/twitter-api GC_MAX_METASPACE_SIZE=256

References
----------

 - [Thorntail Documentation](https://docs.thorntail.io/2.4.0.Final/)
 - [AMQ Core Bridges](https://activemq.apache.org/components/artemis/documentation/latest/core-bridges.html)
 - [Configuration of a MDB using a pooled-connection-factory](https://docs.jboss.org/author/display/WFLY10/Connect+a+pooled-connection-factory+to+a+Remote+Artemis+Server)
 - [Red Hat Java S2I for OpenShift](https://access.redhat.com/documentation/en-us/red_hat_jboss_middleware_for_openshift/3/html/red_hat_java_s2i_for_openshift/)

 