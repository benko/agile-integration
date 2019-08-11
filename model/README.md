Data Model Shared Between Projects
==================================

Overview
--------

This project contains four classes that are related, but only one of which
(Tweet) is shared across two projects and thus needs to be made available as a
JAR library.

Building and Deploying
----------------------

Prior to building and deploying any of the other two projects, install this
library into your local Maven cache by running:

    $ mvn clean package
    ...
    $ mvn install

This should take care of your local build needs.

For OpenShift, that will not do, of course. If you have an option of using a
Nexus mirror deployed to OpenShift, making the dependency available there would
take care of it.

