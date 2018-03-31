package provider;

import com.sun.deploy.model.ResourceProvider;
import com.sun.jersey.core.spi.scanning.PackageNamesScanner;
import org.json.JSONException;
import org.json.JSONObject;
import repository.UserRegistrationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserRegitrationProvider {

    public static JSONObject registerUser(HttpServletRequest request) throws Exception{
        JSONObject result = new JSONObject();
        UserRegistrationRepository userRegistrationRepository = new UserRegistrationRepository();
        HashMap<String, String> userInformations = new HashMap<>();

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String userID = request.getHeader("userid");
        String userType = request.getHeader("usertype");
        String email = request.getHeader("email");
        String tel = request.getHeader("tel");
        String gender = request.getHeader("gender");

        userInformations.put("username", username);
        userInformations.put("password", password);
        userInformations.put("userid", userID);
        userInformations.put("usertype", userType);
        userInformations.put("email", email);
        userInformations.put("tel", tel);
        userInformations.put("gender", gender);

        if(userRegistrationRepository.saveUserInfo(userInformations) == false){
            result.put("error", "Bu kullanıcı adı yada e-posta zaten kullanılmaktadır!");
        }

        result.put("status", 200);
        return result;
    }
}
