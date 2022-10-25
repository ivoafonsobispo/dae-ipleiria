package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.TeacherDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.SubjectBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Teacher;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("subjects") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class SubjectService {
    @EJB
    private SubjectBean subjectBean;

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/all") // means: the relative url path is “/api/courses/all”
    public List<SubjectDTO> getAllSubjectsWS() {
        return toDTOs(subjectBean.getAllSubjects());
    }

    // Converts an entity Course to a DTO Course class
    private SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
            subject.getCode(),
            subject.getName(),
            subject.getCourse().getName(),
            subject.getCourse().getCode(),
            subject.getCourseYear(),
            subject.getScholarYear()
        );
    }

    // Converts an entity Student to a DTO Student class
    private StudentDTO toDTO(Student student) {
        return new StudentDTO(
            student.getUsername(),
            student.getPassword(),
            student.getName(),
            student.getEmail(),
            student.getCourse().getCode(),
            student.getCourse().getName()
        );
    }

    // converts an entire list of entities into a list of DTOs
    private List<SubjectDTO> toDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // converts an entire list of entities into a list of DTOs
    private List<StudentDTO> studentsToDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/{code}")
    public Response getSubjectDetails(@PathParam("code") long code) {
        Subject subject = subjectBean.find(code);
        if (subject != null) {
            return Response.ok(toDTO(subject)).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
            .entity("ERROR_FINDING_STUDENT")
            .build();
    }


    @GET
    @Path("/{code}/students")
    public Response getSubjectStudents(@PathParam("code") long code) {
        Subject subject = subjectBean.find(code);
        if (subject != null) {
            List<StudentDTO> dtos = studentsToDTOs(subject.getStudents());
            return Response.ok(dtos).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
            .entity("ERROR_FINDING_STUDENT")
            .build();
    }


    @GET
    @Path("/{code}/teachers")
    public List<TeacherDTO> getAllTeachersOfSubjectWS(@PathParam("code") long code) {
        return teachersToDTOs(subjectBean.getAllTeachersOfSubject(code));
    }

    private TeacherDTO toDTO(Teacher teacher) {
        return new TeacherDTO(
            teacher.getUsername(),
            teacher.getPassword(),
            teacher.getName(),
            teacher.getEmail(),
            teacher.getOffice()
        );
    }

    private List<TeacherDTO> teachersToDTOs(List<Teacher> teachers) {
        return teachers.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
