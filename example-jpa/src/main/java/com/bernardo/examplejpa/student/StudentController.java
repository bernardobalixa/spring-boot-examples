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
