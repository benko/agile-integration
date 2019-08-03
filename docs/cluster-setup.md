OCP Cluster Set-Up
==================

After having deployed the cluster using Terraform, the following changes were
performed.

Deploying Red Hat SSO
---------------------

Deployment of the SSO itself:

    $ oc new-project sso-internal
    ...
    $ oc new-app sso73-x509-postgresql-persistent \
        -p APPLICATION_NAME=provider \
        -p SSO_HOSTNAME=sso-internal.apps.agile.ocp.aws.p0f.net \
        -p DB_DATABASE=sso \
        -p SSO_ADMIN_USERNAME=ssoadmin \
        -p SSO_ADMIN_PASSWORD="XXXXXXXXXXXXXXXXXX" \
        -p SSO_REALM=ocp-agile \
        -p SSO_SERVICE_USERNAME=ssoclient \
        -p SSO_SERVICE_PASSWORD="XXXXXXXXXXXXXXXXXX" \
        --name=provider

    $ oc get -o yaml route provider > route.yml
    [fix the route]
    $ cat route.yml
    apiVersion: route.openshift.io/v1
    kind: Route
    metadata:
      labels:
        app: provider
        application: provider
        rhsso: 7.3.0.GA
        template: sso73-x509-postgresql-persistent
      name: provider
    spec:
      host: sso-internal.apps.agile.ocp.aws.p0f.net
      tls:
        termination: reencrypt
      to:
        kind: Service
        name: provider
        weight: 100
      wildcardPolicy: None
    $ oc replace route provider -f route.yml
    $ rm -f route.yml

Configuration of the SSO service is at <https://sso-internal.apps.agile.ocp.aws.p0f.net/auth/admin>.

More details are available in [Red Hat SSO for OpenShift documentation](https://access.redhat.com/documentation/en-us/red_hat_single_sign-on/7.3/html/red_hat_single_sign-on_for_openshift/).

Configuring SSO Client for OpenShift
------------------------------------

Creating a new client for OpenShift consists of:

    Realm: Ocp-agile
        -> Clients
        [Create]

    Client ID: agile-ocp
    Client Protocol: openid-connect
    [Save]

In the subsequent client configuration, make sure the following settings apply:

    Enabled: ON
    Access Type: confidential
    Standard Flow Enabled: ON
    Implicit Flow Enabled: OFF
    Direct Access Grants Enabled: OFF
    Service Accounts Enabled: FF
    Authorization Enabled: OFF
    Valid Redirect URIs:
        https://api.agile.ocp.aws.p0f.net:6443/*
        https://console-openshift-console.apps.agile.ocp.aws.p0f.net/*
        https://oauth-openshift.apps.agile.ocp.aws.p0f.net/*
    Advanced Settings:
        Access Token Lifespan: 1 Minutes

Obtain a client secret from the ``Credentials`` tab.

More details are available in [Chapter 5: Configuring OpenShift to use Red Hat Single Sign-On for Authentication](https://access.redhat.com/documentation/en-us/red_hat_single_sign-on/7.3/html/red_hat_single_sign-on_for_openshift/tutorials#OSE-SSO-AUTH-TUTE).

Configuring OpenShift to use Internal SSO
-----------------------------------------

Either using the Web Console or by creating the OAuth CR manually, there are
three required resources:

- a configmap containing the SSO CA certificate
- a secret containing the SSO client key
- an OAuth CR containing all the other settings

The first two can be found in the `openshift-config` project:

    $ oc get -o yaml -n openshift-config cm sso-internal-ca-cert
    apiVersion: v1
    kind: ConfigMap
    metadata:
      name: sso-internal-ca-cert
      namespace: openshift-config
      ...
    data:
      ca.crt: |
        -----BEGIN CERTIFICATE-----
        MIIC7TCCAdWgAwIBAgIBATANBgkqhkiG9w0BAQsFADAmMSQwIgYDVQQDDBtpbmdy
        ...
        z3rKeKoCcLLprIoe++t2XB+/ExjAqnulLQEggby5MgzE
        -----END CERTIFICATE-----

***IMPORTANT***: The key in the above CM **must** be called ``ca.crt`` or it will
not be recognised by the OAuth machinery.

    $ oc get -o yaml -n openshift-config secret openid-client-secret-foo
    apiVersion: v1
    kind: Secret
    metadata:
      generateName: openid-client-secret-
      name: openid-client-secret-ttst9
      namespace: openshift-config
      ...
    data:
      clientSecret: Mz....dk
    type: Opaque

Finally, the CR definition for SSO provider is as follows:

    $ oc get -o yaml oauth cluster
    apiVersion: config.openshift.io/v1
    kind: OAuth
    metadata:
      annotations:
        release.openshift.io/create-only: "true"
      name: cluster
      ...
    spec:
      identityProviders:
      - mappingMethod: claim
        name: sso-internal
        openID:
          ca:
            name: sso-internal-ca-cert
          claims:
            email:
            - email
            name:
            - name
            - full_name
            - username
            preferredUsername:
            - preferred_username
            - username
          clientID: agile-ocp
          clientSecret:
            name: openid-client-secret-ttst9
          extraScopes:
          - email
          - profile
          issuer: https://sso-internal.apps.agile.ocp.aws.p0f.net/auth/realms/ocp-agile
        type: OpenID

More details are available in [Red Hat OpenShift Container Platform documentation](https://docs.openshift.com/container-platform/4.1/), specifically about [Configuring Identity Providers](https://docs.openshift.com/container-platform/4.1/authentication/identity_providers/).

