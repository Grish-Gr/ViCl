package com.mter.vicl.services.classroom;

import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomStudentService classroomStudentService;
    @Autowired
    private ClassroomTeacherService classroomTeacherService;

    @Transactional
    public List<Student> getStudents(Long classroomID, Long userID, Role role
    ) throws NoAuthStudentInClassroomException, NoAuthTeacherInClassroomException {
        Classroom classroom = role.equals(Role.STUDENT)
            ? classroomStudentService.checkStudentInClassroom(userID, classroomID)
            : classroomTeacherService.checkTeacherInClassroom(userID, classroomID);
        return classroom.getRecordStudents().stream().map(RecordStudent::getStudent).toList();
    }

    @Transactional
    public List<Task> getTasks(Long classroomID, Long userID, Role role
    ) throws NoAuthStudentInClassroomException, NoAuthTeacherInClassroomException {
        Classroom classroom = role.equals(Role.STUDENT)
            ? classroomStudentService.checkStudentInClassroom(userID, classroomID)
            : classroomTeacherService.checkTeacherInClassroom(userID, classroomID);
        return classroom.getTasks();
    }
}
