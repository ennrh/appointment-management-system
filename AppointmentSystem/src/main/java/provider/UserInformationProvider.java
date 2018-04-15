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

        String key = "f8eb31a4-8736-41b2-8194-0e8c6785cb54";
        String token = request.getHeader("token");

        if(token != null){
            Claims claim = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            Date date = claim.getExpiration();
            String username = claim.getSubject();
            String password = claim.getAudience();

            if (date.compareTo(new Date()) > 0 && userInfoRepository.checkUser(username, password)){
                result.put("status", 200);
                return result;
            }
        }

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        System.out.print(username + " " + password);

        try {
            if (username == null || password == null) {
                result.put("status", 405);
                return result;
            } else {
                Boolean bool = userInfoRepository.checkUser(username, password);
                if (bool) {
                    result.put("status", 200);

                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.HOUR, 24);

                    String newToken = Jwts.builder()
                            .setSubject(username)
                            .setAudience(password)
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
