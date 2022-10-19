package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    EntityManager em;


    public void create(String username, String password, String name, String email, long course_code) {
        Course course = em.find(Course.class, course_code);

        if (course == null) {
            return;
        }

        Student student = new Student(username, password, name, email, course);
        em.persist(student);
    }

    public List<Student> getAllStudents() {
        // remember, maps to: “SELECT s FROM Student s ORDER BY s.name”
        return (List<Student>) em.createNamedQuery("getAllStudents").getResultList();
    }

    public Student find(String username) {
        return em.find(Student.class, username);
    }

    public void enrollStudentInSubject(String username, long subjectCode) {
        Student student = em.find(Student.class, username);
        Subject subject = em.find(Subject.class, subjectCode);

        Course courseOfStudent = em.find(Course.class, student.getCourse().getCode());
        Course courseOfSubject = em.find(Course.class, subject.getCourse().getCode());

        if (courseOfStudent.equals(courseOfSubject)) {
            subject.addStudent(student);
        }
    }
}
