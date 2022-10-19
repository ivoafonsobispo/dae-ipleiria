package pt.ipleiria.estg.dei.ei.dae.academics.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Teacher extends User implements Serializable {
    @NotNull
    String office;

    @NotNull
    @ManyToMany(mappedBy = "teachers")
    List<Subject> subjects;

    public Teacher() {
        subjects = new LinkedList<>();
    }

    public Teacher(String username, String password, String name, String email, String office) {
        super(username, password, name, email);
        this.office = office;
        subjects = new LinkedList<>();
    }
}
