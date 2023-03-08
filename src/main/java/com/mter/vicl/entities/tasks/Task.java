package com.mter.vicl.entities.tasks;

import com.mter.vicl.entities.classroom.ClassRoom;
import com.mter.vicl.entities.tasks.AnswerTask;
import com.mter.vicl.entities.users.Teacher;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiration")
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "classroom", referencedColumnName = "id")
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<AnswerTask> answers;
}
