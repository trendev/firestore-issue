# firestore-issue

## Pre-req
**service-key-account.json** (including Firestore credentials) must be located in src/main/resources. The folder does not exist in this repository, so please, make it :smile:

## Build the WAR file
`mvn clean install` 

## Deploy on a Glassfish/Payara server and Test
Open a web browser and Go to http://localhost:8080/firestore 
You should see a status message (SUCCESSFULLY_CONNECTED_AND_CLOSED or ERROR).

Undeploy the application and you will get the following severe message in the logs:

`Severe:   The web application [] created a ThreadLocal with key of type [java.lang.ThreadLocal] (value [java.lang.ThreadLocal@48481300]) and a value of type [io.grpc.netty.shaded.io.netty.util.internal.InternalThreadLocalMap] (value [io.grpc.netty.shaded.io.netty.util.internal.InternalThreadLocalMap@69c1f6be]) but failed to remove it when the web application was stopped. Threads are going to be renewed over time to try and avoid a probable memory leak.`
