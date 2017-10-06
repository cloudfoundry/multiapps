# cloud-mta-java-common
Provides common components like parsers, validators, persistence and utilities for [Multi-Target Application (MTA)](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) models. These common components are reuse across different MTA deployer implementations. One such instance is the [Cloud Foundry MTA deploy service](https://github.com/SAP/cf-mta-deploy-service).

# Components

## com.sap.lmsl.slp.java
Provides the REST API and Java XML binding (JAXB) objects for operating MTA applications. This would be in short term refactored to be more Cloud Foundry native like.

## com.sap.activiti.common
Provides utili

## com.sap.cloud.lm.sl.common
TODO

## com.sap.cloud.lm.sl.mta
TODO

## com.sap.cloud.lm.sl.persistence
TODO

## com.sap.cloud.lm.sl.slp
TODO

# Building
## Prerequisites
All components are built with [Apache Maven](http://maven.apache.org/).
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
TODO

# License
Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
This file is licensed under the Apache Software License, v. 2 except as noted otherwise in the [LICENSE](TODO) file.