## REST API documentation

### Overview
Users REST API is based on UserJson entity with following fields:

- "uuid": "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381" - VALIDATED as UUID 
- "name": "jbloch" - VALIDATED as string 4 to 8 characters - NOT NULL
- "firstName": "Josh" - VALIDATED as string 2 to 50 characters - NOT NULL
- "lastName": "Bloch" - VALIDATED as string 2 to 50 characters - NOT NULL
- "emailAddress": "josh@email.com" - VALIDATED as email - NOT NULL
- "birthDate": "1970-08-25" - VALIDATED as date in format yyyy-MM-dd - NOT NULL
- "registeredOn": "2024-05-07" - VALIDATED as date in format yyyy-MM-dd - NULLABLE

### 1. GET
-   **GET `/api/v1/users`**: Retrieves a list of all users.
    -   Example Response:
        ```json
        [
          {
            "uuid": "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381",
            "name": "jbloch",
            "firstName": "Josh",
            "lastName": "Bloch",
            "emailAddress": "josh@email.com",
            "birthDate": "1970-08-25",
            "registeredOn": "2024-05-07"
          },
          {
            "uuid": "2cee318f-4344-429c-9476-6484ef6e276c",
            "name": "jrotten",
            "firstName": "Johny",
            "lastName": "Rotten",
            "emailAddress": "jrotten@email.com",
            "birthDate": "1935-02-15",
            "registeredOn": "2024-04-27"
          }
        ]
        ```
    -   Response Status: 200 (OK) - List of users


-   **GET `/api/v1/users/user?uuid={id}`**: Retrieves a user by UUID.
    -   Example Request: `/api/users/user?uuid=deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381`
    -   Example Response:
        ```json
        {
          "uuid": "deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381",
          "name": "jbloch",
          "firstName": "Josh",
          "lastName": "Bloch",
          "emailAddress": "josh@email.com",
          "birthDate": "1970-08-25",
          "registeredOn": "2024-05-07"
        }
        ```
    -   Response Status: 200 (OK) - User found
    -   Response Status: 400 (Bad request) - Bad request
    -   Response Status: 404 (Not found) - User not found

### 2. CREATE

-   **POST `/api/v1/users/user`**: Creates a new user.
    -   Example Request Body:
        ```json
        {
          "name": "mtrava",
          "firstName": "Michal",
          "lastName": "Trava",
          "emailAddress": "trava@seznam.cz",
          "birthDate": "1972-08-25",
          "registeredOn": "2025-05-07"
        }
        ```
        _Date of registration - "registeredOn" can be ommited - will be set to current date_

    -   Example Response:
        ```json
        {
          "uuid": "b1b44b12-34bc-4ed7-a666-9657b8b8c31b",
          "name": "mtrava",
          "firstName": "Michal",
          "lastName": "Trava",
          "emailAddress": "trava@seznam.cz",
          "birthDate": "1972-08-25",
          "registeredOn": "2025-05-07"
        }
        ```
    -   Response Status: 201 (Created) - User created
    -   Response Status: 400 (Bad request) - Bad request
    -   Response Status: 409 (Conflict) - Conflict with existing user 

### 2. UPDATE
-   **PUT `/api/v1/users/user`**: Updates an existing user.
    -   Example Request: `/api/v1/users/user`
    -   Example Request Body:
        ```json
        {
          "uuid": "f6e4208f-5df4-466e-9225-01f296e2a09c",
          "name": "pbobek",
          "firstName": "Pavel",
          "lastName": "Bobek",
          "emailAddress": "pbob@seznam.cz",
          "birthDate": "1971-08-20",
          "registeredOn": "2024-08-07"
        }
        ```
    -   Response Status: 204 (No Content) - User updated
    -   Response Status: 400 (Bad request) - Bad request/Not found 
    -   Response Status: 409 (Conflict) - Conflict with other user 

### 4. DELETE
-   **DELETE `/api/v1/users/user?uuid={id}`**: Deletes a user by ID.
    -   Example Request: `/api/v1/users/user?uuid=deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381`
    -   Response Status: 204 (No Content) - User deleted
    -   Response Status: 400 (Bad request) - Bad request
    -   Response Status: 404 (Not found) - User not found
---
### Examples of valid requests
Get user by UUID:

http://localhost:8080/api/v1/users/user?uuid=deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381
```
curl http://localhost:8080/api/v1/users/user?uuid=deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381
```

Get all users:

http://localhost:8080/api/v1/users/

    curl -X 'GET' http://localhost:8080/api/v1/users/

Create user:

    curl -X 'POST' \
    'http://localhost:8080/api/v1/users/user' \
    -H 'accept: application/json' \
    -H 'Content-Type: application/json' \
    -d '{
        "name": "mtrava",
        "firstName": "Michal",
        "lastName": "Trava",
        "emailAddress": "trava@seznam.cz",
        "birthDate": "1972-08-25",
        "registeredOn": "2025-05-07"
    }'

Update user:

    curl -X 'PUT' \
    'http://localhost:8080/api/v1/users/user' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d '{
        "uuid": "f6e4208f-5df4-466e-9225-01f296e2a09c",
        "name": "pbobek",
        "firstName": "Pavel",
        "lastName": "Bobek",
        "emailAddress": "pbob@seznam.cz",
        "birthDate": "1971-08-20",
        "registeredOn": "2024-08-07"
    }'

Delete user:

    curl -X 'DELETE' \
    'http://localhost:8080/api/v1/users/user?uuid=deccc52a-4c7d-4f0c-9ba9-e12b6dd3c381' \
    -H 'accept: */*'
---
### Examples of invalid requests

Get user by UUID:

    curl -X 'GET' \
    'http://localhost:8080/api/v1/users/user?uuid=b1b44b12-34bc-4ed7-a666-9657b8b8c31b' \
    -H 'accept: application/json'

Response:

    {
        "type": "UserApi-V1",
        "title": "Not Found",
        "status": 404,
        "detail": "No static resource uuid=[b1b44b12-34bc-4ed7-a666-9657b8b8c31b].",
        "instance": "/api/v1/users/user"
    }

Create user:

    curl -X 'POST' \
    'http://localhost:8080/api/v1/users/user' \
    -H 'accept: application/json' \
    -H 'Content-Type: application/json' \
    -d '{
        "name": "mtrava",
        "firstName": "Michal",
        "lastName": "Trava",
        "emailAddress": "trava@seznam.cz",
        "birthDate": "1972-08-25",
        "registeredOn": "2025-05-07"
    }'

Response:

    {
        "type": "UserApi-V1",
        "title": "Conflict",
        "status": 409,
        "detail": "Conflict with existing user",
        "instance": "/api/v1/users/user",
        "exception": "ExistingUserConflict",
        "message": "Detail: Key (name)=(mtrava) already exists."
    }

Update user:

    curl -X 'PUT' \
    'http://localhost:8080/api/v1/users/user' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d '{
        "uuid": "f6e4208f-5df4-466e-9225",
        "name": "pbobek",
        "firstName": "Pavel",
        "lastName": "Bobek",
        "emailAddress": "pbob@seznam.cz",
        "birthDate": "1971-08-20",
        "registeredOn": "2024-08-07"
    }'

Response:

    {
        "type": "UserApi-V1",
        "title": "Bad Request",
        "status": 400,
        "detail": "Invalid request content.",
        "instance": "/api/v1/users/user",
        "exception": "MethodArgumentNotValidException",
        "message": "[f6e4208f-5df4-466e-9225:must be a valid UUID]"
    }

Delete user:

    curl -X 'DELETE' \
    'http://localhost:8080/api/v1/users/user?uuid=b1b44b12-34bc' \
    -H 'accept: */*'

Response:

    {
        "type": "UserApi-V1",
        "title": "Bad Request",
        "status": 400,
        "detail": "Validation failure",
        "instance": "/api/v1/users/user",
        "exception": "HandlerMethodValidationException",
        "message": "[uuid:must be a valid UUID]"
    }
