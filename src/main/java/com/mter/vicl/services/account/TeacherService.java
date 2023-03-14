package com.mter.vicl.services.account;

import com.mter.vicl.dto.request.ClassroomFormDto;
import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.ClassroomRepository;
import com.mter.vicl.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ClassroomRepository classroomRepository;

    @Transactional
    public List<Classroom> getClassrooms(Long teacherID) throws NoSuchElementException {
        Teacher teacher = teacherRepository.findById(teacherID).orElseThrow();
        return teacher.getClassrooms();
    }

    @Transactional
    public Classroom createClassroom(Long teacherID, ClassroomFormDto classroomForm) throws NoSuchElementException{
        Teacher teacher = teacherRepository.findById(teacherID).orElseThrow();
        Classroom classroom = new Classroom();
        classroom.setTitle(classroomForm.title());
        classroom.setDescription(classroomForm.description());
        classroom.setSecretKey(classroomForm.secretKey());
        classroom.setTeacher(teacher);
        return classroomRepository.save(classroom);
    }
}
