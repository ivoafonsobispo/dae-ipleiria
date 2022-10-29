package pt.ipleiria.estg.dei.ei.dae.academics.ws;

import pt.ipleiria.estg.dei.ei.dae.academics.dtos.StudentDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.dtos.SubjectDTO;
import pt.ipleiria.estg.dei.ei.dae.academics.ejbs.StudentBean;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Student;
import pt.ipleiria.estg.dei.ei.dae.academics.entities.Subject;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;
import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityNotFoundException;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("students") // relative url web path for this service
@Produces({MediaType.APPLICATION_JSON}) // injects header “Content-Type: application/json”
@Consumes({MediaType.APPLICATION_JSON}) // injects header “Accept: application/json”

public class StudentService {
    @EJB
    private StudentBean studentBean;

    @GET // means: to call this endpoint, we need to use the HTTP GET method
    @Path("/all") // means: the relative url path is “/api/students/”
    public List<StudentDTO> getAllStudentsWS() {
        return toDTOs(studentBean.getAllStudents());
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
    private List<StudentDTO> toDTOs(List<Student> students) {
        return students.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Converts an entity Student to a DTO Subject class
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

    // converts an entire list of entities into a list of DTOs
    private List<SubjectDTO> subjectsToDTOs(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/{username}")
    public Response getStudentDetails(@PathParam("username") String username) {
        Student student;
        try {
            student = studentBean.find(username);
        } catch (Exception exception) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
        }
        return Response.ok(toDTO(student)).build();
    }

    @GET
    @Path("/{username}/subjects")
    public Response getStudentSubjects(@PathParam("username") String username) {
        List<SubjectDTO> listSubjects;
        try {
            listSubjects = subjectsToDTOs(studentBean.getSubjects(username));
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("ERROR_FINDING_STUDENT")
                .build();
        }
        return Response.ok(listSubjects).build();
    }

    @POST
    @Path("/")
    public Response create(StudentDTO studentDTO)
        throws Exception, MyEntityExistsException, MyEntityNotFoundException {
        Student student;
        try {
            student = studentBean.create(
                studentDTO.getUsername(),
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
            );
        } catch (Exception e) {
            throw new Exception(e);
        }

        return Response.status(Response.Status.CREATED)
            .entity(toDTO(student))
            .build();
    }

    @PUT
    @Path("/{username}")
    public Response update(@PathParam("username") String username, StudentDTO studentDTO) {
        try {
            studentBean.update(
                username,
                studentDTO.getPassword(),
                studentDTO.getName(),
                studentDTO.getEmail(),
                studentDTO.getCourseCode()
            );
        } catch (Exception exception) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity("STUDENT_UPDATED").build();
    }
}
