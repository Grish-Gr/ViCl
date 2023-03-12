package com.mter.vicl.entities.classroom;

import com.mter.vicl.entities.tasks.Task;
import com.mter.vicl.entities.users.Teacher;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher", referencedColumnName = "id")
    private Teacher teacher;

    @Column(name = "secret_key")
    private String secretKey;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RecordStudent> recordStudents;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<Task> tasks;
}
