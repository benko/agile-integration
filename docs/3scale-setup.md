Red Hat 3scale API Gateway Set-Up Procedure
===========================================

Required Resources
------------------

For the deployment of 3scale on OCP, we need the S3 3scale AMP Template for OpenShift, available from the [templates section of the 3scale GitHub repository](https://github.com/3scale/3scale-amp-openshift-templates).

More information on how to deploy and configure 3scale is available from [Red Hat 3scale Installation Guide](https://access.redhat.com/documentation/en-us/red_hat_3scale_api_management/2.5/html-single/installing_3scale/index).

Setting Up The Project
----------------------

We'll be using the GitHub repository:

    $ export GIT_URL=https://raw.githubusercontent.com/3scale/3scale-amp-openshift-templates/master

Create the template. We need the S3 template.

    $ oc create -f ${GIT_URL}/amp/amp-s3.yml

This is going to be long. Hahaha, not!

    $ oc new-app --name=api-gw \
	    --template=3scale-api-management-s3 \
	    -p WILDCARD_DOMAIN=api.apps.agile.ocp.aws.p0f.net \
	    -p WILDCARD_POLICY=Subdomain

Post-Install Configuration
--------------------------

After deploying 3scale, we need to configure SSO integration.

In the internal SSO, create a new client:

    Realm: Ocp-agile
	-> Clients
	[Create]

    Client ID: 3scale-sso
    Client Protocol: openid-connect
    [Save]

In the subsequent client configuration, make sure the following settings apply:

    Enabled: ON
    Access Type: confidential
    Standard Flow Enabled: ON
    Implicit Flow Enabled: OFF
    Direct Access Grants Enabled: OFF
    Service Accounts Enabled: ON
    Authorization Enabled: OFF
    Valid Redirect URIs:
	https://3scale-admin.api.apps.agile.ocp.aws.p0f.net/*
    Advanced Settings:
	Access Token Lifespan: 1 Minutes
    [Save]

Change the service account roles in the ``Service Account Roles`` tab:

    Client Roles: realm-management
    Available Roles: manage-clients
    [Add selected >>]

Obtain the client secret from the ``Credentials`` tab.

In the 3scale management console, configure the following:

    Account Settings
	-> Users
	-> SSO Integrations
	[New SSO Integration]

    SSO Provider: Red Hat Single Sign-On
    Client: 3scale-sso
    Client Secret: c6...cc
    Realm or Site: https://sso-internal.apps.agile.ocp.aws.p0f.net/auth/realms/ocp-agile
    Do not verify SSL certificate: YES

After saving, test OAuth flow by clicking the ``Test authentication flow``
link. Log in as some existing user.

If successful, make the SSO provider active by clicking ``Publish``.

For accessing 3scale, users do not need any roles. However, to do anything
useful, they have to be assigned at least one role **in the SSO admin
console**.

