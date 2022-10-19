package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "getAllStudents",
        query = "SELECT s FROM Student s ORDER BY s.name" // JPQL
    )
})

public class Student extends User implements Serializable {
    @NotNull
    @ManyToOne
    @JoinColumn(name = "course_code")
    Course course;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "students")
    List<Subject> subjects;

    public Student() {
        course = new Course();
        subjects = new LinkedList<>();
    }

    public Student(String username, String password, String name, String email, Course course) {
        super(username, password, name, email);
        this.course = course;
        subjects = new LinkedList<>();
    }

    public void add(Subject subject) {
        if (subject == null || subjects.contains(subject)) {
            return;
        }
        subjects.add(subject);
    }

    public void remove(Subject subject) {
        if (subject == null || !subjects.contains(subject)) {
            return;
        }
        subjects.remove(subject);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
