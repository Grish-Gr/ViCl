package com.mter.vicl.entities.classroom;

import com.mter.vicl.entities.users.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "student_classroom_record")
public class RecordStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student", referencedColumnName = "id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom", referencedColumnName = "id")
    private Classroom classroom;

    @Temporal(TemporalType.DATE)
    @Column(name = "record_date")
    private Date dateRecord;

    @Enumerated(EnumType.STRING)
    private StatusRecord statusRecord;
}
