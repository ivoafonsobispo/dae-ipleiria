package pt.ipleiria.estg.dei.ei.dae.academics.exceptions.mappers;

import pt.ipleiria.estg.dei.ei.dae.academics.exceptions.MyEntityExistsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class MyEntityExistsExceptionMapper implements ExceptionMapper<MyEntityExistsException> {
    private static final Logger logger =
        Logger.getLogger(MyEntityExistsException.class.getCanonicalName());

    @Override
    public Response toResponse(MyEntityExistsException e) {
        String errorMsg = e.getMessage();
        logger.warning("ERROR: " + errorMsg);
        return Response.status(Response.Status.CONFLICT)
            .entity(errorMsg)
            .build();
    }
}
