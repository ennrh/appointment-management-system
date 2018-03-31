package repository;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;
import java.util.HashMap;

public class UserRegistrationRepository extends repository.BaseRepository {

    public boolean saveUserInfo(HashMap<String, String> userInformations) throws SQLException {
        this.connect();
        this.createStatement();

        if(checkUsernameEmail(userInformations.get("username"),
                userInformations.get("email")) == false){
            return false;
        }
        String password = encriptPassword(userInformations.get("password"));

        this.statement.executeUpdate("insert into Users (Username, UserPassword, UserType)" +
                " values('" + userInformations.get("username") +
                "','" + password + "'," + userInformations.get("usertype") + ")");

        this.statement.executeUpdate("insert into PersonalInformation (UserID, InfoType, InfoValue)\n" +
                "values((select UserID from Users where Username = '" +
                userInformations.get("username") + "'),'email','" +
                userInformations.get("email") + "')");

        this.statement.executeUpdate("insert into PersonalInformation (UserID, InfoType, InfoValue)\n" +
                "values((select UserID from Users where Username = '" +
                userInformations.get("username") + "'),'tel','" +
                userInformations.get("tel") + "')");

        this.statement.executeUpdate("insert into PersonalInformation (UserID, InfoType, InfoValue)\n" +
                "values((select UserID from Users where Username = '" +
                userInformations.get("username") + "'),'gender','" +
                userInformations.get("gender") + "')");

        this.closeConnection();

        return true;
    }

    public String encriptPassword(String psw) {
        String encriptedPsw;
        encriptedPsw = DigestUtils.shaHex(psw);

        return encriptedPsw;
    }

    public boolean checkUsernameEmail(String username, String email){
        try {
            String sql =
                    "select UserID as userid from Users where username = '"
                            + username + "' or (select InfoValue from PersonalInformation where " +
                            "InfoType = 'email') = '" + email +"'";
            resultSet = this.statement.executeQuery(sql);
            System.out.println("SQL: " + sql);
            if(resultSet.next()){
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

}
