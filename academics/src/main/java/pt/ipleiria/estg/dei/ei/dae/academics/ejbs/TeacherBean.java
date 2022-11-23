package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;
import pt.ipleiria.estg.dei.ei.dae.academics.security.Hasher;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TeacherBean {
    @PersistenceContext
    EntityManager em;
    @Inject // import javax.inject.Inject;
    private Hasher hasher;

    public List<Teacher> getAllTeachers() {
        return (List<Teacher>) em.createNamedQuery("getAllTeachers").getResultList();
    }

    public Teacher create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, hasher.hash(password), name, email, office);
        em.persist(teacher);
        return em.find(Teacher.class, username);
    }

    public Teacher update(String username, String password, String name, String email, String office) {
        Teacher teacher = em.find(Teacher.class, username);
        teacher.setPassword(password);
        teacher.setName(name);
        teacher.setEmail(email);
        teacher.setOffice(office);
        return teacher;
    }

    public void associateTeacherToSubject(String username, long subjectCode) {
        Teacher teacher = em.find(Teacher.class, username);
        Subject subject = em.find(Subject.class, subjectCode);
        Course courseOfSubject = em.find(Course.class, subject.getCourse().getCode());

        if (courseOfSubject != null && teacher != null) {
            subject.associate(teacher);
        }
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

    public void delete(String username) {
        Teacher teacher = em.find(Teacher.class, username);
        em.remove(teacher);
    }
}
