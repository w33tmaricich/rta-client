#!/bin/bash

mvn install:install-file -Dfile=log4j-api-2.0-beta8.jar -DgroupId=com.log4j -DartifactId=log4j-api-2.0-beta8 -Dversion=2.0.8 -Dpackaging=jar
mvn install:install-file -Dfile=log4j-core-2.0-beta8.jar -DgroupId=com.log4j -DartifactId=log4j-core-2.0-beta8 -Dversion=2.0.8 -Dpackaging=jar
mvn install:install-file -Dfile=RTAClient.jar -DgroupId=com.skyline.CameraControlServer.Utility.Communications -DartifactId=RTAClient -Dversion=1.0.0 -Dpackaging=jar
