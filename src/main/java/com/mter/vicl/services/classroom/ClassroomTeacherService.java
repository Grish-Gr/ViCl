package com.mter.vicl.services.classroom;

import com.mter.vicl.dto.request.TaskFormDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.classroom.StatusRecord;
import com.mter.vicl.entities.tasks.AnswerTask;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.repositories.*;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private AnswerTaskRepository answerRepository;

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
    public List<RecordStudent> getUnconfirmedStudentsInClassroom(Long teacherID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        return classroom.getRecordStudents().stream()
            .filter(record -> record.getStatusRecord() == StatusRecord.UNCONFIRMED)
            .toList();
    }

    @Transactional
    public List<Task> getTasksInClassroom(Long teacherID, Long classroomID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        return classroom.getTasks();
    }

    @Transactional
    public RecordStudent changeStatusRecordStudent(Long teacherID, Long classroomID, Long studentID, StatusRecord status
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        Student student = studentRepository.findById(studentID).orElseThrow();
        RecordStudent recordStudent = student.getRecordStudents().stream()
            .filter(record -> record.getStudent().equals(student))
            .findAny().orElseThrow();
        if (recordStudent.getClassroom().equals(classroom)){
            recordStudent.setStatusRecord(status);
            return recordRepository.save(recordStudent);
        } else {
            throw  new NoAuthStudentInClassroomException();
        }
    }

    @Transactional
    public RecordStudent addStudentInClassroom(Long teacherID, Long classroomID, Long studentID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        Student student = studentRepository.findById(studentID).orElseThrow();
        RecordStudent recordStudent = new RecordStudent();
        recordStudent.setClassroom(classroom);
        recordStudent.setStudent(student);
        recordStudent.setDateRecord(new Date());
        recordStudent.setStatusRecord(StatusRecord.ACTIVE);
        return recordRepository.save(recordStudent);
    }

    @Transactional
    public Task addTaskInClassroom(Long teacherID, Long classroomId, TaskFormDto taskForm
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomId);
        Task task = new Task();
        task.setTitle(taskForm.title());
        task.setDescription(taskForm.description());
        task.setCreateDate(new Date());
        task.setExpirationDate(taskForm.expirationDate());
        task.setClassroom(classroom);
        return taskRepository.save(task);
    }

    @Transactional
    public List<AnswerTask> getAnswersOnTask(Long teacherID, Long classroomID, Long taskID
    ) throws NoSuchElementException, NoAuthTeacherInClassroomException {
        Task task = taskRepository.findById(taskID).orElseThrow();
        if (classroomID != task.getClassroom().getId()){
            throw new NoSuchElementException("Classroom don't have task");
        }
        checkTeacherInClassroom(teacherID, task.getClassroom().getId());
        return task.getAnswers();
    }

    @Transactional
    public AnswerTask addGradleAnswer(Long teacherID, Long classroomID, Long taskID, Long answerID, byte gradle
    ) throws NoAuthTeacherInClassroomException, NoSuchElementException {
        Classroom classroom = checkTeacherInClassroom(teacherID, classroomID);
        AnswerTask answer = answerRepository.findById(answerID).orElseThrow();
        if (answer.getTask().getId() != taskID){
            throw new NoSuchElementException("Wrong task ID");
        }
        answer.setGradle(gradle);
        return answerRepository.save(answer);
    }
}
