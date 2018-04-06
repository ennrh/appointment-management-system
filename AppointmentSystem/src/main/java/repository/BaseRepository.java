package repository;

import java.sql.*;
import java.util.UUID;

public abstract class BaseRepository {

    protected Statement statement;
    protected Connection connection;
    protected ResultSet resultSet;

    public void connect(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://89.252.184.201:3306/pubtekno_appointmentSystem",
                    "pubtekno_systemadmin","Systemadmin1?");
            statement = connection.createStatement();

        }catch (SQLException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void createStatement(){
        try {
            this.statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public String getKey(){
        String key = null;

        try {
            createStatement();
            resultSet = statement.executeQuery("Select PVALUE as keyy from Properties" +
                    " where PKEY = 'Key'");
            if (resultSet.next()){
                key = resultSet.getString("keyy");
            }else{
                key = UUID.randomUUID().toString();
                statement.executeUpdate("insert into Properties (PVALUE, PKEY)" +
                        " values('Key', '" + key + "')");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return key;
    }
}
