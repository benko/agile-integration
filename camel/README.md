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

Configuring Application Parameters
----------------------------------

There are two sets of properties that need to be configured for the
application to work. They can be set using environment variables or Java
system properties, however, environment variables are the preferred type
of setting as they have been tested much more than the latter.

The Twitter client will need a set of authentication options that will
allow it to communicate to the API:

 - ``TWITTER_CONSUMER_KEY`` or ``agile.camel.consumer.key``
   identifies the consumer against Twitter API
 - ``TWITTER_CONSUMER_SECRET`` or ``agile.camel.consumer.secret``
   authenticates the consumer
 - ``TWITTER_ACCESS_TOKEN`` or ``agile.camel.access.token``
   identifies the application
 - ``TWITTER_ACCESS_SECRET`` or ``agile.camel.access.secret``
   authenticates the application

The translation API at Systran only requires an application key:

 - ``SYSTRAN_APP_KEY`` or ``agile.camel.translate.key``
   authenticates the application against Systran

See below for references to Twitter and Systran API pages. 

There are a couple of optional settings:

 - ``TWITTER_ACCOUNT`` or ``agile.camel.follow.account``
   is the Twitter account to follow (defaults to ``BBCSport``)
 - ``TWITTER_POLL_INTERVAL`` or ``agile.camel.poll.interval``
   is the time between consecutive polls, in milliseconds (defaults to 30s)

As outlined above, for integration purposes, there needs to be an AMQ broker
available to deliver the translated tweets to. The defaults may be suitable
for local execution,  but most probably won't work for OpenShift:

 - ``AMQ_BROKER_URL`` or 	``agile.camel.amq.url`` (defaults to ``tcp://localhost:61616``)
 - ``AMQ_USERNAME`` or ``agile.camel.amq.username`` (defaults to empty)
 - ``AMQ_PASSWORD`` or ``agile.camel.amq.password`` (defaults to empty)

Consult the top-level documentation and the ``amq`` project for how to deploy
and configure a broker.

Running Locally as a SpringBoot App
-----------------------------------

Caveat: you will need to have the ``agile-model`` JAR file available before
this build will succeed. For local use, go to the ``model`` directory and:

    $ cd model/
    $ mvn clean package install

Then build this application using the ``package`` goal: 

    $ cd ../camel/
    $ mvn clean package

Make sure the environment variables are set (or add system properties on the
command line), then execute the resulting JAR:

    $ java -jar ./target/agile-camel-1.0.0.jar

See the configuration section above for more details on configuring the app.

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

Alternatively, you could configure the main build to be a multi-module one, by
removing the context directory from the source config, making sure you set
``ARTIFACT_DIR``, as required for multi-module builds, add the ``-pl`` option
to ``MAVEN_ARGS``, and switching the default goal to ``install`` rather than
``package``:

    $ oc new-app ... \
            --build-env=ARTIFACT_DIR=camel/target \
            --build-env=MAVEN_ARGS="-e -DskipTests -Dcom.redhat.xpaas.repo.redhatga -pl model,camel install"

You will also need an AMQ broker deployment prior to successfully running
this application. Consult the top-level documentation and the ``amq`` project
for more on this.

Having first created a project to accommodate the app, we need a ``configmap``
and/or ``secret`` to load the application settings from.

    $ oc create configmap camel-config \
            --from-literal=TWITTER_ACCOUNT=foo \
            --from-literal=AMQ_BROKER_URL=tcp://broker-amq-tcp:61616
    ...
    $ oc create secret generic camel-auth \
            --from-literal=TWITTER_CONSUMER_KEY=xxxxxxxx \
            --from-literal=TWITTER_CONSUMER_SECRET=xxxxxxxx \
            --from-literal=TWITTER_ACCESS_TOKEN=xxxxxxxx \
            --from-literal=TWITTER_ACCESS_SECRET=xxxxxxxx \
            --from-literal=SYSTRAN_APP_KEY=xxxxxxxx
    ...

Once these are created, we can use ``new-app`` to generate all other artifacts.

    $ oc new-app https://github.com/benko/agile-integration.git \
            --name=twitter-feed \
            --context-dir=camel \
            --strategy=source \
            -i redhat-openjdk18-openshift:1.5 \
            --build-env=MAVEN_MIRROR_URL=http://nexus.apps.mycloud.com/java/
    ...

While the build is taking place, link the required environment variables from
configmap and/or secret for the application. 

    $ oc set env dc/twitter-feed --from=cm/camel-config
    ...
    $ oc set env dc/twitter-feed --from=secret/camel-auth
    ...

This should take care of setting the application environment variables from
respective configmaps and secrets, rather than having them stored in the dc
as literals. 

If your AMQ broker requires authentication, obviously, add the ``AMQ_USERNAME``
and ``AMQ_PASSWORD`` environment variables from the broker configmap/secret:

    $ oc set env dc/twitter-feed --from=secret/broker-auth

Also, make sure that the destination configuration (such as ``security-settings``
and ``address-settings``) are correct to allow the client app to connect and
use the destinations. 

References
----------

 - [Twitter Developer Portal](https://developer.twitter.com/en.html)
 - [Systran API Platform](https://platform.systran.net/)
