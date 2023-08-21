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
