package com.mter.vicl.entities.classroom;

import com.mter.vicl.entities.FileInfo;
import com.mter.vicl.entities.users.Teacher;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "classroom_messages")
public class ClassroomMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "classroom", referencedColumnName = "id")
    private Classroom classroom;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "file_supplements_classroom_message",
        joinColumns = @JoinColumn(name = "classroom_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "file_info_id", referencedColumnName = "id"))
    private List<FileInfo> supplementFiles;
}
