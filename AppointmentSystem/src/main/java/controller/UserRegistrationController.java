package controller;
 
import org.json.JSONException;
import org.json.JSONObject;
import provider.UserInformationProvider;
import provider.UserRegitrationProvider;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.sql.SQLException;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/user")
public class UserRegistrationController {

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUSer(@Context HttpServletRequest request) throws Exception {
		JSONObject data = UserRegitrationProvider.registerUser(request);

		return Response.ok().entity(data.toString()).build();
	}
}