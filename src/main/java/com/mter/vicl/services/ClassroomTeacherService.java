package com.mter.vicl.services;

import com.mter.vicl.dto.request.TaskFormDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.classroom.StatusRecord;
import com.mter.vicl.entities.tasks.AnswerTask;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.*;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClassroomTeacherService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RecordStudentRepository recordRepository;
    @Autowired
    private TaskRepository taskRepository;

    public Classroom checkTeacherInClassroom(Long teacherID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = classroomRepository.findById(classroomID).orElseThrow();
        if (classroom.getTeacher().getId() == teacherID){
            return classroom;
        } else {
            throw new NoAuthTeacherInClassroomException();
        }
    }

    @Transactional
    public List<Student> getStudentsInClassroom(Long teacherID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        return classroom.getRecordStudents().stream()
            .filter(record -> record.getStatusRecord() == StatusRecord.ACTIVE)
            .map(RecordStudent::getStudent).toList();
    }

    @Transactional
    public List<Student> getUnconfirmedStudentsInClassroom(Long teacherID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        return classroom.getRecordStudents().stream()
            .filter(record -> record.getStatusRecord() == StatusRecord.UNCONFIRMED)
            .map(RecordStudent::getStudent).toList();
    }

    @Transactional
    public RecordStudent confirmStudentInClassroom(Long teacherID, Long studentID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        Student student = studentRepository.findById(studentID).orElseThrow();
        RecordStudent recordStudent = student.getRecordStudents().stream()
            .filter(record -> record.getStudent().equals(student))
            .findAny().orElseThrow();
        if (recordStudent.getClassroom().equals(classroom)){
            recordStudent.setStatusRecord(StatusRecord.ACTIVE);
            return recordRepository.save(recordStudent);
        } else {
            throw  new NoAuthStudentInClassroomException();
        }
    }

    @Transactional
    public Task addTaskInClassroom(Long teacherID, TaskFormDto taskForm
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, taskForm.classroomID());
        Task task = new Task();
        task.setTitle(taskForm.title());
        task.setDescription(taskForm.description());
        task.setCreateDate(new Date());
        task.setExpirationDate(taskForm.expirationDate());
        task.setClassroom(classroom);
        return taskRepository.save(task);
    }

    @Transactional
    public List<AnswerTask> getAnswersOnTask(Long teacherID, Long taskID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Task task = taskRepository.findById(taskID).orElseThrow();
        checkTeacherInClassroom(teacherID, task.getClassroom().getId());
        return task.getAnswers();
    }
}
