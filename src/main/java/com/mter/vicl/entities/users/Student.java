package com.mter.vicl.entities.users;

import com.mter.vicl.entities.classroom.ClassRoom;
import com.mter.vicl.entities.classroom.RecordStudent;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "students")
public class Student extends User {

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RecordStudent> recordStudents;
}
