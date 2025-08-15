# user-manager [![Java CI with Maven](https://github.com/MichalTravnicek/user-manager/actions/workflows/maven.yml/badge.svg)](https://github.com/MichalTravnicek/user-manager/actions/workflows/maven.yml)
Simple user management with REST and Postgres using JDBC DAO


### REST API:
- OpenAPI/Swagger console: 
http://localhost:8080/swagger-ui/index.html

### How to build and run:
- App is configured with two profiles - default and "docker".
Default profile is for launching against localhost. Docker profile is autoselected.
- Docker compose files for database and application are separated.
So that lifecycle of database is decoupled from application.
- For application to build and run Postgres database must be present - localhost or docker.
Application compose performs build with tests and then runs jar 

#### Database:

    start:
    docker compose -f docker/database/docker-compose.yml up -d

    shutdown:
    docker compose -f docker/database/docker-compose.yml down

#### Application:

    start:
    docker compose -f docker/application/docker-compose.yml up -d

    shutdown and cleanup:
    docker compose -f docker/application/docker-compose.yml down -v

    to force rebuild:
    $ docker compose -f docker/application/docker-compose.yml up --build --force-recreate

