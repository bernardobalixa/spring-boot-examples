# Spring Boot JPA and REST Example

## Run the application

```shell
 $ ./mvnw spring-boot:run
```

## Test the application

Configure your PostgreSQL database first.

### Postman or Insomnia

I recommend that you use Postman or Insomnia to test this example.

#### Add a Student:

**POST** - localhost:8080/api/students

Body:

```json
{
    "email": "student1@gmail.com",
    "name": "Student 1",
    "birthDate": "2000-10-10"
}
```

#### Get All Students:

**GET** - localhost:8080/api/students

.
#### Get By Name:

**GET** - localhost:8080/api/students/byName<b>?name=Student 1</b>

.
#### Get By Id:

**GET** - localhost:8080/api/students/byId<b>?id=1</b>

.
#### Edit a Student:

**PUT** - localhost:8080/api/students<b>?id=1</b>

Body:

```json
{
    "email": "student01@gmail.com",
    "name": "Student 01",
    "birthDate": "2000-10-15"
}
```

.
#### Delete a Student:

**DELETE** - localhost:8080/api/students<b>?id=1</b>

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
    <artifactId>example-jpa</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>example-jpa</name>
    <description>Spring Boot JPA usage</description>
    <properties>
        <java.version>17</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
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

### ExampleJpaApplicationTests.java

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

package com.bernardo.examplejpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ExampleJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleJpaApplication.class, args);
    }

}
```

### Student.java

```java
package com.bernardo.examplejpa.student;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity(name = "student")
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    @Transient
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + age +
                '}';
    }
}
```

### StudentRepository.java

```java
package com.bernardo.examplejpa.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findOneById(Long id);

    List<Student> findByName(String name);

    Student findOneByEmail(String email);

}
```

### StudentService.java

```java
package com.bernardo.examplejpa.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    public Student findOneByEmail(String email) {
        return studentRepository.findOneByEmail(email);
    }

    public Student findOne(Long id) {
        return studentRepository.findOneById(id);
    }

    @Transactional
    public Student save(Student student) {
        return studentRepository.saveAndFlush(student);
    }

    public Student edit(Student newStudent, Long id) {
        Student toEditStudent = studentRepository.findOneById(id);
        toEditStudent.setName(newStudent.getName());
        toEditStudent.setEmail(newStudent.getEmail());
        toEditStudent.setBirthDate(newStudent.getBirthDate());
        return studentRepository.saveAndFlush(toEditStudent);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return studentRepository.existsById(id);
    }

}
```

### StudentController.java

```java
package com.bernardo.examplejpa.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return ResponseEntity.ok().body(students);
    }

    @GetMapping("/byId")
    public ResponseEntity<Student> getStudentById(@RequestParam("id") Long id) {
        Student student = studentService.findOne(id);

        if (student != null)
            return ResponseEntity.ok().body(student);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/byName")
    public ResponseEntity<?> getStudentsByName(@RequestParam("name") String name) {
        if (name == null)
            return ResponseEntity.badRequest().body("Invalid name!");

        List<Student> students = studentService.findByName(name);
        return ResponseEntity.ok().body(students);
    }

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        try {
            if (studentService.findOneByEmail(student.getEmail()) != null) {
                return ResponseEntity.badRequest().body("There is already a student with the same email registered!");
            } else {
                Student savedStudent = studentService.save(student);
                if (savedStudent != null)
                    return ResponseEntity.ok().body(savedStudent);
                else
                    return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editStudent(@RequestBody Student newStudent, @RequestParam("id") Long id) {
        if (id == null)
            return ResponseEntity.badRequest().body("Invalid id");

        try {
            if (!studentService.existsById(id))
                return ResponseEntity.badRequest().body("There is no student with the given id.");

            Student toCheckStudent = studentService.findOneByEmail(newStudent.getEmail());
            if ((toCheckStudent != null && !toCheckStudent.getId().equals(id))) {
                return ResponseEntity.badRequest().body("There is already a student with the same email registered!");
            } else {
                Student savedStudent = studentService.edit(newStudent, id);
                if (savedStudent != null && savedStudent.getId().equals(id))
                    return ResponseEntity.ok().body(savedStudent);
                else
                    return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeStudent(@RequestParam("id") Long id) {
        try {
            if (studentService.existsById(id)){
                studentService.delete(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
```

### application.properties

```properties
# Server Config
server.port = 8080

# Datasource Config
spring.datasource.url = jdbc:postgresql://localhost:5432/students_db
spring.datasource.username = root
spring.datasource.password = root

# JPA Config
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```