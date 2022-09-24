package pt.ipleiria.estg.dei.ei.dae.academics.ejbs;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
@Startup
@Singleton
public class ConfigBean {
    @EJB
    StudentBean studentBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        // Creates Student in DB
        studentBean.create("ivoafonsobispo","yesyes","Ivo Afonso Bispo","ivoafobispo@gmail.com");
    }
}
