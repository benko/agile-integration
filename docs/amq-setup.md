Red Hat AMQ Broker Set-Up Procedure
===================================

Required Resources
------------------

Whatever project we decide to deploy AMQ to, we first need to create the
associated image streams and at least one template. Both are available from the
[JBoss AMQ Container Images GitHub repository](https://github.com/jboss-container-images/jboss-amq-7-broker-openshift-image/).

More information on how to deploy AMQ on OpenShift is available from the
[Deploying AMQ Broker on OpenShift Container Platform](https://access.redhat.com/documentation/en-us/red_hat_amq/7.4/html/deploying_amq_broker_on_openshift_container_platform/index) document.

Deploying the Broker
--------------------

First, let's make typing everything a bit easier:

    $ export AMQ_GIT_URL=https://raw.githubusercontent.com/jboss-container-images/jboss-amq-7-broker-openshift-image/
    $ export AMQ_REL=74-7.4.0.GA

We also need a project:

    $ oc new-project amq

Creating the image streams requires authentication. Create the authentication
secret for accessing the ``registry.redhat.io`` container registry by first
logging into it locally, then creating a ``dockerconfigjson`` secret containing
the ``auth.json`` file created by ``podman`` while logging in.

    $ podman login -u myusername -p mypassword registry.redhat.io
    $ oc create secret generic rhregistry \
        --from-file=.dockerconfigjson=/run/user/${UID}/containers/auth.json \
        --type=kubernetes.io/dockerconfigjson
    $ podman logout registry.redhat.io

Now we can import the image streams:

    $ oc create -f ${AMQ_GIT_URL}/${AMQ_REL}/amq-broker-7-image-streams.yaml

Additionally, we can now create at least one of the following templates:

 - ``amq-broker-74-ssl.yaml``
 - ``amq-broker-74-custom.yaml``
 - ``amq-broker-74-persistence.yaml``
 - ``amq-broker-74-persistence-ssl.yaml``
 - ``amq-broker-74-persistence-clustered.yaml``
 - ``amq-broker-74-persistence-clustered-ssl.yaml``

We picked the ``amq-broker-74-persistence.yaml`` one as it's the simplest one
of the persistent brokers to deploy.

    $ oc create -f ${AMQ_GIT_URL}/${AMQ_REL}/templates/amq-broker-74-persistence.yaml

    $ oc new-app --template=amq-broker-74-persistence \
            -p APPLICATION_NAME=amq \
            -p AMQ_USER=amqadmin \
            -p AMQ_REQUIRE_LOGIN=true

That takes care of it!

