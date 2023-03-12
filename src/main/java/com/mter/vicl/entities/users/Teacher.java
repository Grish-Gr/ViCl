package com.mter.vicl.entities.users;

import com.mter.vicl.entities.classroom.Classroom;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "teachers")
public class Teacher extends User{

    @OneToMany(mappedBy = "teacher", fetch = FetchType.EAGER)
    private List<Classroom> classrooms;
}
