package repository;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

public class UserInfoRepository extends BaseRepository{

    public UserInfoRepository(){
        connect();
    }

    public boolean checkUser(String username, String password){
        try{
            this.createStatement();
            resultSet = statement.executeQuery("select Username as username," +
                    " UserPassword as password from Users");
            while (resultSet.next()){
                System.out.println("DBPassword: " + resultSet.getString("password")
                + " Password:" + DigestUtils.shaHex(password));
                if(resultSet.getString("username").contains(username) &&
                        resultSet.getString("password")
                                .contains(DigestUtils.shaHex(password))){
                    return true;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public String getUserID(String username){
        String id = "";
        try {
            this.connect();
            this.createStatement();

            resultSet = statement.executeQuery("select UserID as id from Users" +
                    " where Username = '" + username +"'");
            resultSet.next();
            id = resultSet.getString("id");

            this.closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }

        return id;
    }

}
