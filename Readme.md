
The loan application system determines the amount of loan applicants can withdraw based on their credit rating and monthly income.

Technologies

- Spring Boot (JPA, Web, Devtools, Validation, Security)

- Spring Framework

- PostgreSQL

- Maven

- Javax

- Jsonwebtoken

- Lombok


Application starts on localhost port 8080

| HTTP METHOD |                       PATH                        |                   USAGE                   |
|:-----------:|:-------------------------------------------------:|:-----------------------------------------:|
|     GET     |        http://localhost:8080/user/auth/all        |    get all users (preauthorize admin)     |
|     GET     |            http://localhost:8080/user/            |          get user which is login          |
|     GET     |      http://localhost:8080/user/{identity}/       |        get user by identity number        |
|     GET     |       http://localhost:8080/user/{id}/auth        | get user by id Admin(preauthorize admin)  |
|     GET     | http://localhost:8080/user/application/{identity} |          get Application result           |
|    PATCH    |          http://localhost:8080/user/auth          |           update user password            |
|     PUT     |            http://localhost:8080/user/            |                update user                |
|   DELETE    |       http://localhost:8080/user//{id}/auth       |                delete user                |
|     PUT     |       http://localhost:8080/user/{id}/auth        | update user authorize(preauthorize admin) |
|    POST     |          http://localhost:8080/register           |            register a new user            |
|    POST     |            http://localhost:8080/login            |        login with registered user         |

