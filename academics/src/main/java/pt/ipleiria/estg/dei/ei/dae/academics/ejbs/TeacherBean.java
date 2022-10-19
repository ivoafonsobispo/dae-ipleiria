package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TeacherBean {
    @PersistenceContext
    EntityManager em;

    public void create(String username, String password, String name, String email, String office) {
        Teacher teacher = new Teacher(username, password, name, email, office);
        em.persist(teacher);
    }
}
