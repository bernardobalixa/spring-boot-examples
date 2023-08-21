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
