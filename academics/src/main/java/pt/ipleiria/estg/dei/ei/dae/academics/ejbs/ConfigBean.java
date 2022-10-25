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
    @EJB
    SubjectBean subjectBean;
    @EJB
    AdministratorBean administratorBean;
    @EJB
    TeacherBean teacherBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        // Creates Courses in DB
        courseBean.create(9119, "Engenharia Informatica");
        courseBean.create(9200, "Engenharia dos Engenheiros");

        // Creates Student in DB
        studentBean.create("ivoafonsobispo", "yesyes", "Ivo Afonso Bispo", "ivoafobispo@gmail.com", 9119);
        studentBean.create("anamartin", "agostosa", "Ana Luisa Pinto Martin", "anamartin@gmail.com", 9119);

        // Creates Subject in DB
        subjectBean.create(1, "Filosofia", courseBean.find(9119), 2001, 1);
        subjectBean.create(2, "Matematica", courseBean.find(9119), 2005, 2);

        studentBean.enrollStudentInSubject("ivoafonsobispo", 1);
        studentBean.enrollStudentInSubject("anamartin", 1);

        administratorBean.create("josematias", "omatias", "Jose Matias", "josematias@mail.pt");

        teacherBean.create("mariajoana", "mariaejoana", "Maria Joana", "mariajoana@mail.pt", "L-31");

        teacherBean.associateTeacherToSubject("mariajoana", 1);
    }
}
