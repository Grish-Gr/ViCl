package com.mter.vicl.entities.tasks;

import com.mter.vicl.entities.FileInfo;
import com.mter.vicl.entities.classroom.Classroom;
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
    private Classroom classroom;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<AnswerTask> answers;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "file_supplements_task",
        joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "file_info_id", referencedColumnName = "id"))
    private List<FileInfo> supplementFiles;
}
