package provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.json.JSONException;
import org.json.JSONObject;
import repository.UserInfoRepository;
import repository.UserRegistrationRepository;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class UserInformationProvider {

    public static JSONObject getUserInformation(HttpServletRequest request) throws JSONException {

        JSONObject result = new JSONObject();
        UserInfoRepository userInfoRepository = new UserInfoRepository();

        String key = userInfoRepository.getKey();
        String token = request.getHeader("token");

        if(token != null){
            Date date = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration();
            if (date.compareTo(new Date()) > 0){
                result.put("status", 200);
                return result;
            }
        }

        String username = request.getHeader("username");
        String password = request.getHeader("password");

        try {
            if (username == null || password == null) {
                result.put("status", 405);
                return result;
            } else {
                Boolean bool = userInfoRepository.checkUser(username, password);
                if (bool) {
                    result.put("status", 200);

                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date()); // Now use today date.
                    c.add(Calendar.HOUR, 24); // Adds 15 days

                    String newToken = Jwts.builder()
                            .setSubject(username)
                            .setExpiration(c.getTime())
                            .signWith(SignatureAlgorithm.HS512, key)
                            .compact();

                    result.put("token", newToken);
                } else {
                    result.put("status", 405);
                    result.put("error", "Wrong username or password!");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(result.toString());
        return result;
    }
}
