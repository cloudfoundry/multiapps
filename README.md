
# MultiApps [![Build Status](https://travis-ci.org/cloudfoundry-incubator/multiapps.svg?branch=master)](https://travis-ci.org/cloudfoundry-incubator/multiapps)

Provides common components like parsers, validators, persistence and utilities for [Multi-Target Application (MTA)](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) models. These common components are reused across different MTA deployer implementations. One such instance is the [Cloud Foundry MTA deploy service](https://github.com/SAP/cf-mta-deploy-service).

# Components

## com.sap.lmsl.slp.java
Provides the REST API and Java XML binding (JAXB) objects for operating [MTA](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) applications. This would be in short term refactored to be more Cloud Foundry native like.

## com.sap.activiti.common
Contains utilities and abstractions on top of [Activiti](https://www.activiti.org/) BPMN engine. These are used for modelling of MTA operation workflows like deployment, update, undeployment.

## com.sap.cloud.lm.sl.common
Contains different utilities and exception types.

## com.sap.cloud.lm.sl.mta
Contains [MTA](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) model objects for different specification versions, parsers and validators. 

## com.sap.cloud.lm.sl.persistence
Contains utilities for upload and processing of file artifacts. These are used for initial upload of the [MTA](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) archive and descriptors and their processing as part of the deployment.

## com.sap.cloud.lm.sl.slp
Contains the REST API infrastructure for managing [MTA](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) applications. It adapts the workflow processed implemented using [Activiti](https://www.activiti.org/) to a REST API interface and objects. This is only abstract infrastructure, whereas the concrete implementation of the REST API is available in [CF MTA deploy service](https://github.com/SAP/cf-mta-deploy-service).

# Building
## Prerequisites
All components are built with Java 8 and [Apache Maven](http://maven.apache.org/).
## Compiling and Packaging
To build `com.sap.lmsl.slp.java`, run the following command from the `com.sap.lmsl.slp.java` sub-directory:
```
$ mvn clean install
```
To build `com.sap.activiti.common`, run the following command from the `com.sap.activiti.common` sub-directory after building `com.sap.lmsl.slp.java`:
```
$ mvn clean install
```
To build `com.sap.cloud.lm.sl.common`, `com.sap.cloud.lm.sl.mta`, `com.sap.cloud.lm.sl.persistence` and `com.sap.cloud.lm.sl.slp`, run the following command from the root directory after building `com.sap.lmsl.slp.java` and `com.sap.activiti.common`:
```
$ mvn clean install
```
# How to obtain support
If you need any support, have any question or have found a bug, please report it in the [GitHub bug tracking system](https://github.com/SAP/cloud-mta-java-common/issues). We shall get back to you.

# License
Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
This file is licensed under the Apache Software License, v. 2 except as noted otherwise in the [LICENSE](https://github.com/SAP/cloud-mta-java-common/blob/master/LICENSE) file.
