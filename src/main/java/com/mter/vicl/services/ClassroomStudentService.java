package com.mter.vicl.services;

import com.mter.vicl.dto.request.AnswerFormDto;
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

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClassroomStudentService {

    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AnswerTaskRepository answerRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Classroom checkStudentInClassroom(Long studentID, Long classroomID) throws NoSuchElementException, NoAuthStudentInClassroomException {
        Classroom classroom = classroomRepository.findById(classroomID).orElseThrow();
        classroom.getRecordStudents().stream()
            .filter(record -> record.getStudent().getId() == studentID)
            .findAny()
            .orElseThrow(NoAuthStudentInClassroomException::new);
        return classroom;
    }

    @Transactional
    public List<Student> getStudentsInClassroom(Long classroomID, Long studentID
    ) throws NoSuchElementException, NoAuthStudentInClassroomException{
        Classroom classroom = checkStudentInClassroom(studentID, classroomID);
        return classroom.getRecordStudents().stream()
            .filter(record -> record.getStatusRecord() == StatusRecord.ACTIVE)
            .map(RecordStudent::getStudent).toList();
    }

    @Transactional
    public List<Task> getTasksInClassroom(Long classroomID, Long studentID
    ) throws NoSuchElementException, NoAuthStudentInClassroomException{
        Classroom classroom = checkStudentInClassroom(studentID, classroomID);
        return classroom.getTasks();
    }

    @Transactional
    public AnswerTask putAnswerTask(Long studentID, AnswerFormDto answerForm) throws NoSuchElementException, NoAuthStudentInClassroomException {
        Task task = taskRepository.findById(answerForm.taskID()).orElseThrow();
        Classroom classroom = checkStudentInClassroom(studentID, task.getClassroom().getId());
        Student student = studentRepository.findById(studentID).orElseThrow();
        if (classroom.equals(task.getClassroom())){
            AnswerTask answerTask = new AnswerTask();
            answerTask.setAnswer(answerForm.answer());
            answerTask.setTask(task);
            answerTask.setStudent(student);
            return answerRepository.save(answerTask);
        } else {
            throw new NoAuthStudentInClassroomException();
        }
    }
}
