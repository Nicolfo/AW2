# WA2 - LAB4 -G33
We have created a docker image starting from the base image of keycloak containing
our security configuration.
Three users are already present in the realm "AW2-Auth-Realm"
* u1 with role manager and password="password"
* u2 with role expert and password="password"
* u3 with role customer and password="password"

In order to log in our application we provide in the frontend a login form that will show directly the JWT token as a response.
This token can be used by the client to authenticate the requests made to the ticketing controller with the proper APIs.

Since the postgres database is empty, profiles can be added (only inside the profile table) in the following way
* one by one in the frontend by compiling the addProfile form
* on the project with the data present inside SQL INIT folder

The port in which each service is exposed are:
* keycloak server port 8080
* springboot backend and static frontend port 8081
* postgres database port 5432