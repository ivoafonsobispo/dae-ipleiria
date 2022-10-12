package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
@Startup
@Singleton
public class ConfigBean {

    @EJB
    CourseBean courseBean;
    @EJB
    StudentBean studentBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        // Creates Courses in DB
        courseBean.create(9119,"Engenharia Informatica");
        courseBean.create(9200,"Engenharia dos Engenheiros");

        // Creates Student in DB
        studentBean.create("ivoafonsobispo","yesyes","Ivo Afonso Bispo","ivoafobispo@gmail.com",9119);
    }
}
