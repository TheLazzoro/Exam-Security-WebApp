package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dtos.ForumThreadDTO;
import dtos.ForumThreadsDTO;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;
import facades.Forum_Thread_Facade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/thread")
public class ForumThreadResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final Forum_Thread_Facade FACADE =  Forum_Thread_Facade.getForumThreadFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Context
    SecurityContext securityContext;

    @Path("")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getThreadsAll() {
        ForumThreadsDTO activities =  FACADE.getAll();
        return Response
                .ok()
                .entity(GSON.toJson(activities))
                .build();
    }

    @Path("{threadID}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getThreadById(@PathParam("threadID") long playerID) throws NotFoundException {
        return Response
                .ok()
                .entity(GSON.toJson(FACADE.getById(playerID)))
                .build();
    }

    @Path("create")
    @POST
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String content) throws API_Exception, NotFoundException {
        String username = securityContext.getUserPrincipal().getName();
        ForumThreadDTO toCreate = GSON.fromJson(content, ForumThreadDTO.class);
        // Any username can be passed to the jsonObject,
        // so we make sure to set the correct user here.
        toCreate.getAuthor().setUsername(username);
        ForumThreadDTO created = FACADE.create(toCreate);
        return Response
                .ok()
                .status(201)
                .entity(created)
                .build();
    }

}