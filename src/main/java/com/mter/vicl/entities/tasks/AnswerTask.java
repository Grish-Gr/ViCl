package com.mter.vicl.entities.tasks;

import com.mter.vicl.entities.FileInfo;
import com.mter.vicl.entities.users.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "task_answers")
public class AnswerTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "answer")
    private String answer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "task", referencedColumnName = "id")
    private Task task;

    @Column(name = "gradle")
    private byte gradle;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation")
    private Date createDate;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "file_supplements_answer",
        joinColumns = @JoinColumn(name = "answer_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "file_info_id", referencedColumnName = "id"))
    private List<FileInfo> supplementFiles;
}
