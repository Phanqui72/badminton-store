# Base meta

This is a service reserved for authentication and authorization based on Oauth2 protocol.

## Tech stack

-   Spring-boot 2.3
-   MySQL
-   Swagger
-   Docker

## Service configuration

- Database config
    ```
    spring.datasource.url=jdbc:postgresql://<db host>:<db port>/<db name>
    spring.datasource.username=<username>
    spring.datasource.password=<password>
    ```

-   Initial data
    Currently all initial data will define on `/resource/data.sql`

## BUILD

-   Jar file
    `mvn clean package`

- Docker images
  ``` docker build . --tag [image-tag-name]```
  Ex: ```docker build . --tag user-service-be-v1.0```

## LAUNCH APPLICATION

-   From Jar
    `java -jar [app jar file] [-Dspring.profiles.active=test]`

-  From docker images
   ```docker run -it -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=[profile-name]" [image-tag-version]```

   Ex: ```docker run -it -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=dev" user-service-be-v1.0```

## Contact point

If you have any problem when rebuild application feel free to contact persons in below:

| Contact name | Email | Position          |
| - |------| ----------------- |
|   |      | Software engineer |
