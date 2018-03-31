package controller;

import org.json.JSONObject;
import provider.UserRegitrationProvider;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class LoginController {

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUSer(@Context HttpServletRequest request) throws Exception {
        JSONObject data = UserRegitrationProvider.registerUser(request);

        return Response.ok().entity("selam").build();
    }
}
