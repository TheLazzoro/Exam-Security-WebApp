package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ForumThreadDTO;
import errorhandling.API_Exception;
import facades.Forum_Thread_Facade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Path("/user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final UserFacade FACADE = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @Context
    SecurityContext securityContext;

    @Path("image-upload")
    @POST
    //@RolesAllowed("user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(@FormDataParam("file") InputStream uploadedInputStream,
                                @FormDataParam("file") FormDataContentDisposition fileDetail) throws IOException, API_Exception {

        // Grab correct username.
        String username = securityContext.getUserPrincipal().getName();
        FACADE.setProfileImage(username, uploadedInputStream, fileDetail);

        return Response
                .ok()
                .status(201)
                .build();
    }

}