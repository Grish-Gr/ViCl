package com.mter.vicl.services.storage;

import com.mter.vicl.dto.response.ResourceDto;
import com.mter.vicl.entities.FileInfo;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.ClassroomMessage;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.classroom.StatusRecord;
import com.mter.vicl.entities.tasks.AnswerTask;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.repositories.*;
import com.mter.vicl.services.exceptions.FileStorageException;
import com.mter.vicl.services.storage.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class FileService implements StorageService {

    @Autowired
    private FileManager fileManager;
    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AnswerTaskRepository answerTaskRepository;
    @Autowired
    private ClassroomRepository classroomRepository;
    @Autowired
    private ClassroomMessageRepository classroomMessageRepository;

    @Override
    public List<FileInfo> uploadAll(MultipartFile... files) {
        return Arrays.stream(files).map(file -> {
            try {
                return upload(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    @Override
    public FileInfo upload(MultipartFile file) throws IOException {
        String pathToFile = fileManager.saveFileIntoStorage(file);
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setPathToFile(pathToFile);
        fileInfo.setUploadDate(new Date());
        fileInfo.setSize(file.getSize());
        return fileInfoRepository.save(fileInfo);
    }

    @Transactional
    public ResourceDto downloadSupplementTask(Long taskID, Long fileID, Long userID, Role role) throws IOException {
        Task task = taskRepository.findById(taskID).orElseThrow();
        Classroom classroom = task.getClassroom();
        boolean isAuthIntoClassroom = role.equals(Role.STUDENT)
            ? classroom.getRecordStudents().stream().anyMatch(recordStudent ->
                recordStudent.getStatusRecord().equals(StatusRecord.ACTIVE)
                    && recordStudent.getStudent().getId() == userID
            )
            : classroom.getTeacher().getId() == userID;
        if (isAuthIntoClassroom){
            return downloadFile(fileID);
        } else {
            throw new AuthenticationServiceException("No access to the resource");
        }
    }

    @Transactional
    public ResourceDto downloadSupplementAnswer(Long answerID, Long fileID, Long userID, Role role) throws IOException {
        AnswerTask answerTask = answerTaskRepository.findById(answerID).orElseThrow();
        if (role.equals(Role.STUDENT)){
            if (answerTask.getStudent().getId() == userID){
                return downloadFile(fileID);
            } else {
                throw new AuthenticationServiceException("");
            }
        } else {
            Classroom classroom = answerTask.getTask().getClassroom();
            if (classroom.getTeacher().getId() == userID){
                return downloadFile(fileID);
            } else {
                throw new AuthenticationServiceException("");
            }
        }
    }

    @Transactional
    public ResourceDto downloadSupplementClassroomMessage(Long classroomMessageID, Long fileID, Long userID, Role role) throws IOException {
        ClassroomMessage message = classroomMessageRepository.findById(classroomMessageID).orElseThrow();
        Classroom classroom = message.getClassroom();
        if (role.equals(Role.STUDENT)){
            boolean inClassroom = classroom.getRecordStudents().stream()
                .filter(record -> record.getStatusRecord().equals(StatusRecord.ACTIVE))
                .anyMatch(record -> record.getStudent().getId() == userID);
            if (inClassroom){
                return downloadFile(fileID);
            } else {
                throw new AuthenticationServiceException("");
            }
        } else {
            if (classroom.getTeacher().getId() == userID){
                return downloadFile(fileID);
            } else {
                throw new AuthenticationServiceException("");
            }
        }
    }

    @Override
    public ResourceDto downloadFile(Long fileID) throws IOException {
        FileInfo fileInfo = fileInfoRepository.findById(fileID).orElseThrow();
        Resource resource = fileManager.downloadFileIntoStorage(fileInfo.getPathToFile());
        return new ResourceDto(resource, fileInfo);
    }

    @Override
    public FileInfo getInfoByID(Long fileID) {
        return null;
    }
}
