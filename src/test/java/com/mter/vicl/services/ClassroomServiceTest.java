package com.mter.vicl.services;

import com.mter.vicl.entities.classroom.Classroom;
import com.mter.vicl.entities.classroom.RecordStudent;
import com.mter.vicl.entities.classroom.StatusRecord;
import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Role;
import com.mter.vicl.entities.users.Student;
import com.mter.vicl.entities.users.Teacher;
import com.mter.vicl.repositories.ClassroomRepository;
import com.mter.vicl.repositories.StudentRepository;
import com.mter.vicl.repositories.TeacherRepository;
import com.mter.vicl.services.classroom.ClassroomService;
import com.mter.vicl.services.classroom.ClassroomStudentService;
import com.mter.vicl.services.exceptions.NoAuthStudentInClassroomException;
import com.mter.vicl.services.exceptions.NoAuthTeacherInClassroomException;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {

    @Mock
    private ClassroomRepository classroomRepository;
    @InjectMocks
    private ClassroomService classroomService;

    @BeforeEach
    public void initClassroomInDB(){
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setRole(Role.TEACHER);

        Student student = new Student();
        student.setId(1);
        student.setName("Student John");
        student.setRole(Role.STUDENT);

        Classroom classroom = new Classroom();
        classroom.setId(1);
        classroom.setTeacher(teacher);
        classroom.setTitle("Title");

        RecordStudent recordStudent = new RecordStudent();
        recordStudent.setClassroom(classroom);
        recordStudent.setStudent(student);
        recordStudent.setStatusRecord(StatusRecord.ACTIVE);

        Task task = new Task();
        task.setClassroom(classroom);
        task.setId(1L);
        task.setTitle("Title task");

        classroom.setTasks(List.of(task));
        classroom.setRecordStudents(List.of(recordStudent));

        when(classroomRepository.findById(1L)).thenReturn(Optional.of(classroom));
    }

    @Test
    public void testGetTasksAtStudent() throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        List<Task> tasks = classroomService.getTasks(1L, 1L, Role.STUDENT);

        Assertions.assertEquals(tasks.size(), 1);
        Assertions.assertEquals("Title task", tasks.get(0).getTitle());
    }

    @Test
    public void testGetTasksAtTeacher() throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        List<Task> tasks = classroomService.getTasks(1L, 1L, Role.TEACHER);

        Assertions.assertEquals(tasks.size(), 1);
        Assertions.assertEquals("Title task", tasks.get(0).getTitle());
    }

    @Test
    public void testGetTasksAtUnauthorizedStudent() {
        Assertions.assertThrows(NoAuthStudentInClassroomException.class,
            () -> classroomService.getTasks(1L, 2L, Role.STUDENT)
        );
    }

    @Test
    public void testGetTasksAtUnauthorizedTeacher() {
        Assertions.assertThrows(NoAuthTeacherInClassroomException.class,
            () -> classroomService.getTasks(1L, 2L, Role.TEACHER)
        );
    }

    @Test
    public void testGetStudentsAtStudent() throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        List<Student> tasks = classroomService.getStudents(1L, 1L, Role.STUDENT);

        Assertions.assertEquals(tasks.size(), 1);
        Assertions.assertEquals(tasks.get(0).getName(), "Student John");
    }

    @Test
    public void testGetStudentsAtTeacher() throws NoAuthTeacherInClassroomException, NoAuthStudentInClassroomException {
        List<Student> tasks = classroomService.getStudents(1L, 1L, Role.TEACHER);

        Assertions.assertEquals(tasks.size(), 1);
        Assertions.assertEquals(tasks.get(0).getName(), "Student John");
    }
}
