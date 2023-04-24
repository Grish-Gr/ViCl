package com.mter.vicl.services.classroom;

import com.mter.vicl.dto.request.AnswerFormDto;
import com.mter.vicl.entities.FileInfo;
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
import com.mter.vicl.services.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Autowired
    private StorageService storageService;

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
    public AnswerTask putAnswerTask(
        Long studentID,
        Long classroomID,
        Long taskID,
        AnswerFormDto answerForm,
        MultipartFile... files
    ) throws NoSuchElementException, NoAuthStudentInClassroomException {
        Task task = taskRepository.findById(answerForm.taskID()).orElseThrow();
        Classroom classroom = checkStudentInClassroom(studentID, classroomID);
        if (task.getClassroom().getId() != classroomID || task.getId() != taskID){
            throw new NoSuchElementException("Wrong task ID");
        }
        Student student = studentRepository.findById(studentID).orElseThrow();
        if (classroom.equals(task.getClassroom())){
            List<FileInfo> supplementFiles = storageService.uploadAll(files);
            AnswerTask answerTask = new AnswerTask();
            answerTask.setAnswer(answerForm.answer());
            answerTask.setTask(task);
            answerTask.setSupplementFiles(supplementFiles);
            answerTask.setStudent(student);
            return answerRepository.save(answerTask);
        } else {
            throw new NoAuthStudentInClassroomException();
        }
    }
}
