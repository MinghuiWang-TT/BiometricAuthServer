package com.alex.auth;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.alex.auth.model.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

public class ThrowablesMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(ThrowablesMapper.class);

    @Override public Response toResponse(Throwable arg0) {

        Throwable root = Throwables.getRootCause(arg0);

        ErrorResponse error = new ErrorResponse();

        if (!StringUtils.isEmpty(root.getMessage())) {
            error.setMessage(root.getMessage());
        } else {
            error.setMessage(arg0.getMessage());
        }
        Response resp = Response.serverError().entity(error).build();

        if (root instanceof IllegalArgumentException) {

            error.setStatus(400);
            resp =
                Response.status(Status.BAD_REQUEST).entity(error).type(MediaType.APPLICATION_JSON)
                    .encoding("UTF-8").build();

        } else if (root instanceof NotFoundException) {

            error.setStatus(404);
            resp = Response.status(Status.NOT_FOUND).entity(error).type(MediaType.APPLICATION_JSON)
                .encoding("UTF-8").build();

        } else {
            error.setStatus(500);
        }

        logger.error(error.getMessage(), root);
        return resp;
    }
}
