package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;

import pt.ipleiria.estg.dei.ei.dae.academics.entities.Course;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CourseBean {

    @PersistenceContext
    EntityManager em;

    public Course create(long code, String name) throws MyEntityExistsException {
        if (em.find(Course.class, code) != null)
            throw new MyEntityExistsException(code + " - Course already Exists");


        Course course = new Course(code, name);
        em.persist(course);
        return em.find(Course.class, code);
    }

    public List<Course> getAllCourses() {
        // remember, maps to: “SELECT c FROM Course c ORDER BY c.name”
        return (List<Course>) em.createNamedQuery("getAllCourses").getResultList();
    }

    public Course find(long code) {
        return em.find(Course.class, code);
    }
}
