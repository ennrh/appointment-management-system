package provider;

import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;

public class UserInformationProvider {

    public static JSONObject getUserInformation(HttpServletRequest request,  MultivaluedMap<String, String> form){
        JSONObject result = new JSONObject();



        return result;
    }
}
