package org.example.util;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    private static final Logger LOG = Logger.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException exception) {
        LOG.warn("Resource not found: " + exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new GlobalExceptionMapper.ErrorResponse("Not Found", "The requested resource was not found."))
                .build();
    }
}