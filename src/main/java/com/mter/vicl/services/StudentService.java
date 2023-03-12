package com.mter.vicl.services;

import com.mter.vicl.dto.request.AnswerFormDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.classroom.StatusRecord;
import com.mter.vicl.entities.tasks.AnswerTask;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.repositories.*;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StudentService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RecordStudentRepository recordRepository;

    @Transactional
    public List<Classroom> getClassrooms(Long studentID) throws NoSuchElementException{
        return recordRepository.findByStudent(studentID).stream()
            .filter(record -> record.getStudent().getId() == studentID)
            .map(RecordStudent::getClassroom)
            .toList();
    }

    @Transactional
    public Classroom recordInClassroom(Long studentID, String secretKey) throws NoSuchElementException{
        Student student = studentRepository.findById(studentID).orElseThrow();
        Classroom classroom = classroomRepository.findBySecretKey(secretKey).orElseThrow();
        RecordStudent recordStudent = new RecordStudent();
        recordStudent.setStudent(student);
        recordStudent.setClassroom(classroom);
        recordStudent.setDateRecord(new Date());
        recordStudent.setStatusRecord(StatusRecord.UNCONFIRMED);
        recordRepository.save(recordStudent);
        return classroom;
    }
}
