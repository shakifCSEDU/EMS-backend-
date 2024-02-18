package com.example.todo.controller;

import com.example.todo.dto.StudentDto;
import com.example.todo.dto.TeacherDto;
import com.example.todo.dto.TeacherStudentDto;
import com.example.todo.dto.TodoDto;
import com.example.todo.service.StudentService;
import com.example.todo.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    // Add Student info to the Student Table
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-student")
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto studentDto){
        StudentDto savedStudent = studentService.addStudent(studentDto);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // Add Teacher info to the Teacher table
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add-teacher")
    public ResponseEntity<TeacherDto> addTeacher(@RequestBody TeacherDto teacherDto){
        TeacherDto savedTeacher = teacherService.addTeacher(teacherDto);
        return new ResponseEntity<>(savedTeacher,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>>getAllStudents(){
        List<StudentDto>studentDtos = studentService.getAllStudents();
        return ResponseEntity.ok(studentDtos);
    }

    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDto>>getAllTeachers(){
        List<TeacherDto>teacherDtos = teacherService.getAllTeachers();
        return ResponseEntity.ok(teacherDtos);
    }



    // fetch all students based on teacher_id
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/teachers/{id}/students")
    public ResponseEntity<List<StudentDto>>getAllAddedStudents(@PathVariable("id") Long teacher_id){

        List<StudentDto> studentDtos = teacherService.findStudents(teacher_id);
        return ResponseEntity.ok(studentDtos);
    }

    // Teacher can delete Request
    @PreAuthorize("hasAnyRole('TEACHER')")
    @PostMapping("/teachers/{id}/delete")
    public ResponseEntity<String>deleteStudent(@PathVariable("id") Long teacher_id, @RequestBody TeacherStudentDto teacherStudentDto){

        teacherService.deletePendingOrAccepted(teacherStudentDto);
        return ResponseEntity.ok("Remove Successfully!");
    }


    // Teacher can accept Request
    @PreAuthorize("hasAnyRole('TEACHER')")
    @GetMapping("/teachers/{id}/accept")
    public ResponseEntity<String>acceptStudent(@PathVariable("id") Long teacher_id,@RequestBody TeacherStudentDto teacherStudentDto){
        teacherService.acceptPendingRequest(teacherStudentDto);
        return ResponseEntity.ok("Teacher Accept the request.");

    }



}