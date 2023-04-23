package com.mter.vicl.services.classroom;

import com.mter.vicl.dto.request.ClassroomMessageDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.ClassroomMessage;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.classroom.StatusRecord;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.repositories.ClassroomRepository;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomRepository classroomRepository;


    @Transactional
    public List<Student> getStudents(Long classroomID, Long userID, Role role
    ) throws NoAuthStudentInClassroomException, NoAuthTeacherInClassroomException {
        Classroom classroom = role.equals(Role.STUDENT)
            ? checkStudentInClassroom(userID, classroomID)
            : checkTeacherInClassroom(userID, classroomID);
        return classroom.getRecordStudents().stream()
            .filter(recordStudent -> recordStudent.getStatusRecord().equals(StatusRecord.ACTIVE))
            .map(RecordStudent::getStudent).toList();
    }

    @Transactional
    public List<Task> getTasks(Long classroomID, Long userID, Role role
    ) throws NoAuthStudentInClassroomException, NoAuthTeacherInClassroomException {
        Classroom classroom = role.equals(Role.STUDENT)
            ? checkStudentInClassroom(userID, classroomID)
            : checkTeacherInClassroom(userID, classroomID);
        return classroom.getTasks();
    }

    @Transactional
    public ClassroomMessageDto getMessages(Long classroomID, Long userID, Role role
    ) throws NoAuthStudentInClassroomException, NoAuthTeacherInClassroomException {
        Classroom classroom = role.equals(Role.STUDENT)
            ? checkStudentInClassroom(userID, classroomID)
            : checkTeacherInClassroom(userID, classroomID);
        return ClassroomMessageDto.from(classroom.getMessages(), classroom);
    }

    @Transactional
    public Classroom checkStudentInClassroom(Long studentID, Long classroomID
    ) throws NoSuchElementException, NoAuthStudentInClassroomException {
        Classroom classroom = classroomRepository.findById(classroomID).orElseThrow();
        classroom.getRecordStudents().stream()
            .filter(record -> record.getStudent().getId() == studentID && record.getStatusRecord() == StatusRecord.ACTIVE)
            .findAny()
            .orElseThrow(NoAuthStudentInClassroomException::new);
        return classroom;
    }

    @Transactional
    public Classroom checkTeacherInClassroom(Long teacherID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = classroomRepository.findById(classroomID).orElseThrow();
        if (classroom.getTeacher().getId() == teacherID){
            return classroom;
        } else {
            throw new NoAuthTeacherInClassroomException();
        }
    }
}
