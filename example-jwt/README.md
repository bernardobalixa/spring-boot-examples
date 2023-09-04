# Spring Boot Security Example

## Run the application

```shell
 $ ./mvnw spring-boot:run
```

## Test routes

### Register User
localhost:8080/api/auth/register

### Register Admin
localhost:8080/api/auth/registerAdmin

### Login
localhost:8080/api/auth/login - Will return the token in header (Authorization)

### Test User
localhost:8080/api/test/user - Pass the token in header (Authorization)

### Test Admin
localhost:8080/api/test/admin - Pass the token in header (Authorization)