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
 
Since the ticketDTO is required in the body of most of the API this is it structure
  ```yaml
  {
    "ticketId" : "UUID of the ticket",
    "description" : "a string containing the description of the issue",
    "priority" :"priority of the ticket as an integer",
    "status" : "status of the ticket OPEN/REOPENED/RESOLVED/CLOSED/IN PROGRESS",
    "customerUsername":"username_of_the_customer",
    "workerUsername":"username_of_the_assigned_expert"
  }
  ```

These are the ticket API:
* POST /API/ticket/create need a description in the body in order to create a ticket, authentication with role Client is needed
* POST /API/ticket/resolve need a ticketDTO in the body in order to change the status of the ticket to resolve, authentication with role Manager is needed
* POST /API/ticket/close need a ticketDTO in the body in order to change the status of the ticket to closed, authentication with role Manager is needed
* POST /API/ticket/start/{priority} expect a number as a path variable, used to set the ticket priority,and a request body with this format:

  ```yaml
  {
    "workerUsername" :"username_of_the_assigned_expert",
    "ticket" : ticketDTO_object
  }
  ```
  in order to change the status of the ticket to in_progress and assign a worker, only request made with role *Manager* are accepted. 
* POST /API/ticket/reopen need a ticketDTO in the body in order to change the status of the ticket to reopened, authentication with role Manager is needed
* POST /API/ticket/stop need a ticketDTO in the body in order to change the status of the ticket to open, authentication with any role is required

All the api returns a TicketDTO with the update status (or an error with detail information of what went wrong)

