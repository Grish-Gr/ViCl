package com.mter.vicl.entities.tasks;

import com.mter.vicl.entities.users.Student;
import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne
    @JoinColumn(name = "student", referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "task", referencedColumnName = "id")
    private Task task;

    @Column(name = "gradle")
    private byte gradle;
}
