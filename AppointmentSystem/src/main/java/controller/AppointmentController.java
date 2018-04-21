package controller;

import org.json.JSONObject;
import provider.AppointmentProvider;
import provider.UserInformationProvider;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/appointment")
public class AppointmentController {

    @POST
    @Path("/take")
    @Produces(MediaType.APPLICATION_JSON)
    public Response takeAppointment(@Context HttpServletRequest request) throws Exception {
        JSONObject data = AppointmentProvider.takeAppointment(request);
        return Response.ok().entity(data.toString()).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization, auth-user")
                .header("Access-Control-Allow-Credentials", "true").build();
    }

    @POST
    @Path("/getAppointments")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointments(@Context HttpServletRequest request) throws Exception {
        JSONObject data = AppointmentProvider.getAppointments(request);
        return Response.ok().entity(data.toString()).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization, auth-user")
                .header("Access-Control-Allow-Credentials", "true").build();
    }

    @POST
    @Path("/openAppointment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response openAppointment(@Context HttpServletRequest request) throws Exception{
        JSONObject data = AppointmentProvider.openAppointment(request);
        return Response.ok().entity(data.toString()).header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Allow-Headers",
                        "origin, content-type, accept, authorization, auth-user")
                .header("Access-Control-Allow-Credentials", "true").build();
    }
}
