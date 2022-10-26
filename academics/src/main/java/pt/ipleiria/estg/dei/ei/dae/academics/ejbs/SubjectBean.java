package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class SubjectBean {

    @PersistenceContext
    EntityManager em;

    public void create(long code, String name, Course course, int courseYear, int scholarYear) {
        Subject subject = new Subject(code, name, course, courseYear, scholarYear);
        em.persist(subject);
    }

    public List<Subject> getAllSubjects() {
        return (List<Subject>) em.createNamedQuery("getAllSubjects").getResultList();
    }

    public Subject find(long code) {
        return em.find(Subject.class, code);
    }

    public List<Teacher> getAllTeachersOfSubject(long code) {
        Subject subject = em.find(Subject.class, code);
        if (subject == null)
            return null;
        Hibernate.initialize(subject.getTeachers());
        return subject.getTeachers();
    }

    public List<Student> getAllStudents(long code) {
        Subject subject = em.find(Subject.class, code);
        return subject.getStudents();
    }
}
