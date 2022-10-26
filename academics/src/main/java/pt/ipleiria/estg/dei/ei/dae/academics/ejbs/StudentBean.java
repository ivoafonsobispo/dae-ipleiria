package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    EntityManager em;

    public Student create(String username, String password, String name, String email, long course_code)
        throws MyEntityExistsException, MyEntityNotFoundException {
        Course course;
        Student student;
        try {
            course = em.find(Course.class, course_code);
        } catch (Exception exception) {
            throw new MyEntityNotFoundException("Course does not Exist");
        }
        try {
            em.find(Student.class, username);
        } catch (Exception exception) {
            throw new MyEntityExistsException("Student already Exists");
        }
        student = new Student(username, password, name, email, course);
        em.persist(student);
        course.add(student);
        return em.find(Student.class, username);
    }

    public Student update(String username, String password, String name, String email, long courseCode)
        throws MyEntityExistsException, MyEntityNotFoundException {
        Student student;
        Course course;
        try {
            course = em.find(Course.class, courseCode);
        } catch (Exception exception) {
            throw new MyEntityNotFoundException("COURSE DOES NOT EXIST");
        }
        try {
            student = em.find(Student.class, username);
        } catch (Exception exception) {
            throw new MyEntityExistsException("STUDENT ALREADY EXISTS");
        }
        em.lock(student, LockModeType.OPTIMISTIC);

        student.setPassword(password);
        student.setName(name);
        student.setEmail(email);
        student.setCourse(course);
        return student;
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

    public List<Subject> getSubjects(String username) {
        Student student = em.find(Student.class, username);
        if (student == null) {
            return null;
        }
        Hibernate.initialize(student.getSubjects());
        return student.getSubjects();
    }
}
