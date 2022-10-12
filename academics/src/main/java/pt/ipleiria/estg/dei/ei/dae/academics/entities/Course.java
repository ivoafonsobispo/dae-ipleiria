package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllCourses",
                query = "SELECT c FROM Course c ORDER BY c.code" // JPQL
        )
})

@Table(
        name = "courses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"})
)
public class Course implements Serializable {
    @Id
    long code;
    @NotNull
    String name;
    @NotNull
    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    List<Student> students;

    public Course() {
        code = -1;
        name = "";
        students = new LinkedList<>();
    }

    public Course(Long code, String name) {
        this.code = code;
        this.name = name;
        students = new LinkedList<>();
    }

    public void addStudent(Student student) {
        if (student == null || students.contains(student)) {
            return;
        }
        students.add(student);
    }

    public void removeStudent(Student student) {
        if (student == null || !students.contains(student)) {
            return;
        }
        students.remove(student);
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
