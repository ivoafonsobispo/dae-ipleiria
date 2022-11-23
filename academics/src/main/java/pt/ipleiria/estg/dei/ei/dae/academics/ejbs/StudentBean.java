package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyConstraintViolationException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class StudentBean {
    @PersistenceContext
    EntityManager em;
    @Inject // import javax.inject.Inject;
    private Hasher hasher;

    public Student create(String username, String password, String name, String email, long course_code)
            throws MyEntityExistsException, MyEntityNotFoundException, MyConstraintViolationException {
        Course course;
        Student student;
        course = em.find(Course.class, course_code);

        if (course == null)
            throw new MyEntityNotFoundException(course_code + " - Course does not Exist");

        if (em.find(Student.class, username) != null)
            throw new MyEntityExistsException(username + " - Student already Exists");

        try {
            student = new Student(username, hasher.hash(password), name, email, course);
            em.persist(student);
            course.add(student);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
        return em.find(Student.class, username);
    }

    public Student update(String username, String password, String name, String email, long courseCode)
            throws MyEntityExistsException, MyEntityNotFoundException {
        Student student;
        Course course;
        course = em.find(Course.class, courseCode);
        if (course == null)
            throw new MyEntityNotFoundException(courseCode + " - Course does not Exist");

        student = em.find(Student.class, username);

        if (student == null)
            throw new MyEntityExistsException(username + " - Student does not Exist");

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
        Student student = em.find(Student.class, username);
        Hibernate.initialize(student.getSubjects());
        return student;
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

    public void delete(String username, long courseCode) throws MyEntityNotFoundException, MyConstraintViolationException {
        Student student = em.find(Student.class, username);
        if (student == null)
            throw new MyEntityNotFoundException(username + " - Student does not Exist");
        Course course = em.find(Course.class, courseCode);
        try {
            course.remove(student);
            em.remove(student);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }
}
