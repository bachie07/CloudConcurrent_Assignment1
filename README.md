#Cloud Concurrent Assignment 1 - Speech to text service
#Gia Bach Pham, student ID: 3160100


##Overview

Spring boot web service that convert speech to text. User will record directly in browser, and backend forwards it to OpenAI's
'gpt-4o-mini-transcribe' API, which return the transcribed text for display

The service also has administrative and statistics endpoints ( uptime, graceful shutdown and global token usage)
per the accompanying OpenAPI spec and its built to handle 200+ concurrent HTTP requests without crashing. 



##Architecture

The current app is split into three kind of classes:

*Controllers* (cloudconcurrent_assigment1.controller)
-handle incoming HTTP requests, each one only know how to talk to the browser, not how data is stored or computed


*Services* ( cloudconcurrent_assigment1.services )
-hold shared data and logic, right now the main one is "AppStateService", which stores three things: when the server started,
flag for whether shutdown has been requested, and return the total token used

*Exception handling (cloudconcurrent_assigment1.exception) 
-the one class that catches errors from any controller and turns them into a proper JSON response instead of 
a blank error page. 

"AppStateService" is created once when the app starts ( Spring managed via @Component). Every controller
that uses this is givena reference to that same single object through the constructor instead of creating its own copy. 


##Configuration (local vs TITAN)
Environment specific settings are handled through spring profiles. Two profiles exists alongside "application.properties"

-"application-local.properties": used for local development. Sets a file upload limit (25mb)

-"application-titan.properties": used when deployed. Sets a stricter upload limit (10mb)

Which profile is active is controlled by "SPRING_PROFILES_ACTIVE" env variable - sets to "local" when local runs.

The OPENAI key is handled purely through env variables since its a secret rather than a configuration setting. 


##Concurrency Approach:



##API endpoints

| Method | Path                        | Description                              |
|--------|-----------------------------|-------------------------------------------|
| GET    | `/`                          | Serves the frontend page                 |
| POST   | `/api/transcribe`            | Accepts audio, returns transcribed text  |
| GET    | `/api/v1/admin/uptime`       | Server start time and uptime             |
| POST   | `/api/v1/admin/shutdown`     | Requests graceful server shutdown        |
| GET    | `/api/v1/global/stats`       | Total tokens used since server start     |



##Deployment difficulties and lesson learned


## Running the project 