# Spring Boot Security Example

## Run the application

```shell
 $ ./mvnw spring-boot:run
```

## Test the application

For this example I recommend using a web browser. But if you prefer, you can use Postman or Insomnia.

#### Auth:

User - username: **user** | password: **password**

Admin - username: **admin** | password: **password_admin**

.
#### Free route:

**GET** - localhost:8080/free

.
#### User and Admin route:

**GET** - localhost:8080/user

.
#### Only Admin route:

**GET** - localhost:8080/admin

## Files

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.2</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.bernardo</groupId>
    <artifactId>example-security</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>example-security</name>
    <description>Spring Boot Security usage Example</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

### ExampleSecurityApplication.java

```java

/*
 * Copyright (c) 2023 Bernardo Balixa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bernardo.examplesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleSecurityApplication.class, args);
    }

}
```

### BasicConfiguration.java

```java
package com.bernardo.examplesecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class BasicConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/user").hasAnyRole("USER_ROLE", "ADMIN_ROLE")
                                .requestMatchers("/admin").hasRole("ADMIN_ROLE")
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults());


        return http.build();

    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER_ROLE")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("password_admin"))
                .roles("ADMIN_ROLE")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
```

### TestController.java

```java
package com.bernardo.examplesecurity.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/free")
    public ResponseEntity<String> freeRoute() {
        return ResponseEntity.ok().body("This is a free route.");
    }

    @GetMapping("/user")
    public ResponseEntity<String> userRoute() {
        return ResponseEntity.ok().body("This is a route that needs authentication from user or admin role.");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminRoute() {
        return ResponseEntity.ok().body("This is a route that needs authentication from admin role.");
    }

}

```

### application.properties

```properties
server.port = 8080
```