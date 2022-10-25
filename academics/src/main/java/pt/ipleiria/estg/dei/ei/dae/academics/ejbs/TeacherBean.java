package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TeacherBean {
    @PersistenceContext
    EntityManager em;

    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) em.createNamedQuery("getAllTeachers").getResultList();
    }

    public void create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, password, name, email, office);
        em.persist(teacher);
    }

    public void update(String username, String password, String name, String email, String office) {
        Teacher teacher = em.find(Teacher.class, username);
        if (teacher == null)
            return;

        teacher.setPassword(password);
        teacher.setName(name);
        teacher.setEmail(email);
        teacher.setOffice(office);
    }

    public boolean associateTeacherToSubject(String username, long subjectCode) {
        Teacher teacher = em.find(Teacher.class, username);
        Subject subject = em.find(Subject.class, subjectCode);

        Course courseOfSubject = em.find(Course.class, subject.getCourse().getCode());

        if (courseOfSubject != null && teacher != null) {
            subject.associate(teacher);
            return true;
        }
        return false;
    }

    public void dissociateTeacherToSubject(String username, long subjectCode) {
        Teacher teacher = em.find(Teacher.class, username);
        Subject subject = em.find(Subject.class, subjectCode);

        Course courseOfSubject = em.find(Course.class, subject.getCourse().getCode());

        if (courseOfSubject != null && teacher != null) {
            subject.dissociate(teacher);
        }
    }

    public List<Subject> getAllSubjectsOfTeacher(String username) {
        Teacher teacher = em.find(Teacher.class, username);
        if (teacher == null)
            return null;
        Hibernate.initialize(teacher.getSubjects());
        return teacher.getSubjects();
    }


    public Teacher find(String username) {
        return em.find(Teacher.class, username);
    }

    public boolean delete(String username) {
        Teacher teacher = em.find(Teacher.class, username);
        if (teacher == null)
            return false;
        em.remove(teacher);
        return true;
    }
}
