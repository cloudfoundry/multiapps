
# MultiApps [![Build Status](https://travis-ci.org/cloudfoundry-incubator/multiapps.svg?branch=master)](https://travis-ci.org/cloudfoundry-incubator/multiapps)

Provides common components like parsers, validators, persistence and utilities for [Multi-Target Application (MTA)](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) models. These common components are reused across different MTA deployer implementations. One such instance is the [Cloud Foundry MTA deploy service](https://github.com/cloudfoundry-incubator/multiapps-controller).

# Components

## com.sap.cloud.lm.sl.common
Contains different utilities and exception types.

## com.sap.cloud.lm.sl.mta
Contains [MTA](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) model objects for different specification versions, parsers and validators. 

## com.sap.cloud.lm.sl.persistence
Contains utilities for upload and processing of file artifacts. These are used for initial upload of the [MTA](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) archive and descriptors and their processing as part of the deployment.

# Building

## Prerequisites

All components are built with Java 8 and [Apache Maven](http://maven.apache.org/).

## Compiling and Packaging

Run the following command from the root directory of the project:
```
$ mvn clean install
```

# How to obtain support
If you need any support, have any questions or have found a bug, please report it in the [GitHub bug tracking system](https://github.com/cloudfoundry-incubator/multiapps/issues). We shall get back to you.

# License
Copyright (c) 2017 SAP SE or an SAP affiliate company. All rights reserved.
This file is licensed under the Apache Software License, v. 2 except as noted otherwise in the [LICENSE](https://github.com/cloudfoundry-incubator/multiapps/blob/master/LICENSE) file.
