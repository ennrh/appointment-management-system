package repository;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.json.JSONException;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AppointmentRepository extends BaseRepository{

    public AppointmentRepository(){

    }

    public boolean checkAppointment(String randevuAlanID, String randevuVerenID,
                                    String randevuTarihi) throws SQLException {
        this.connect();
        String query = "select count(*) as count from Appointments where " +
                "(StudentID = null or StudentID = 0) and LecturerID = '" + randevuVerenID + "' and StartDateTime = '"
                + randevuTarihi + "'";
        System.out.println("Query: " + query);
        resultSet = this.statement.executeQuery(query);
        resultSet.next();
        if(Integer.parseInt(resultSet.getString("count")) > 0){
            this.closeConnection();
            return true;
        }else{
            this.closeConnection();
            return false;
        }

    }

    public void takeAppointment(String randevuAlanID, String randevuVerenID,
                                String randevuTarihi) throws SQLException {
        this.connect();
        this.createStatement();

        this.statement.executeUpdate("update Appointments set AppointmentStatus = 2 where " +
                "LecturerID = '" + randevuVerenID + "' and " +
                "StartDateTime = '" + randevuTarihi + "'");
        this.statement.executeUpdate("update Appointments set StudentID = '" + randevuAlanID
                + "' where " +
                "LecturerID = '" + randevuVerenID + "' and " +
                "StartDateTime = '" + randevuTarihi + "'");

        this.closeConnection();
    }

    public HashMap<String, ArrayList<String>> getAllAppointments(String startDate, String endDate, String randevuVerenID) throws SQLException {
        this.connect();
        this.createStatement();

        ArrayList<String> dates   = new ArrayList<String>();
        ArrayList<String> lengths = new ArrayList<String>();
        ArrayList<String> status  = new ArrayList<String>();

        resultSet = this.statement.executeQuery("select StartDateTime as startdatetime," +
                " Length as length, AppointmentStatus as status from Appointments where " +
                "StartDateTime between '" + startDate + "' and '" + endDate + "' and " +
                "LecturerID = '" + randevuVerenID + "'");

        while (resultSet.next()){
            dates.add(resultSet.getString("startdatetime"));
            lengths.add(resultSet.getString("length"));
            status.add(resultSet.getString("status"));
        }

        HashMap<String, ArrayList<String>> tmp =
                new HashMap<>();

        tmp.put("lengths",lengths);
        tmp.put("dates", dates);
        tmp.put("status", status);

        this.closeConnection();
        return tmp;
    }

    public boolean openSingleAppointment(String startDate, String id,
                                         String length) throws SQLException {
        this.connect();
        this.createStatement();

        if(checkAppointment(startDate, id) == false){
            return false;
        }

        statement.executeUpdate("insert into Appointments (LecturerID, " +
                "StartDateTime, Length, AppointmentStatus, StudentID) values("+ id + "," +
                "'" + startDate + "'," + length + ",1,0)");

        this.closeConnection();

        return true;
    }

    public boolean openWeeklyAppointment(String startDate, String endDate,
                                   org.json.JSONArray days, Integer parameter,
                                   String id, String length) throws SQLException, JSONException {
        boolean result = true;
        this.connect();
        this.createStatement();

        resultSet = statement.executeQuery("select TIMESTAMPDIFF(DAY, " +
                "'" + startDate + "', '" + endDate + "') as different");
        resultSet.next();
        Integer dayDifference = resultSet.getInt("different");
        ArrayList<Integer> day = new ArrayList<>();

        for(int i=0; i<days.length(); i++){
            day.add(((org.json.JSONObject) days.get(i)).getInt("day"));
        }

        while (dayDifference > 0){
                for(int i=0; i<day.size()-1; i++){
                    if(checkAppointment(startDate, id) == false){
                        return false;
                    }
                    statement.executeUpdate("insert into Appointments (LecturerID," +
                            "StartDateTime, Length, AppointmentStatus, StudentID) values" +
                            "(" + id + ",'"+ startDate +"'," + length +",1,0)");
                    resultSet = statement.executeQuery("select '" + startDate + "'" +
                            " + INTERVAL " + (day.get(i+1) - day.get(i)) + " DAY as newDay");
                    resultSet.next();
                    startDate = resultSet.getString("newDay");
                    if(checkDayDifference(startDate,endDate) == false){
                        break;
                    }
                }

                if(checkDayDifference(startDate,endDate) == false){
                    break;
                }

                if(checkAppointment(startDate, id) == false){
                    return false;
                }

                statement.executeUpdate("insert into Appointments (LecturerID," +
                        "StartDateTime, Length, AppointmentStatus, StudentID) values" +
                        "(" + id + ",'"+ startDate +"'," + length +",1,0)");

                resultSet = statement.executeQuery("select '" + startDate + "'" +
                        "+ INTERVAL " + (8 - day.get(day.size()-1)) + " DAY as newDay");
                resultSet.next();
                startDate = resultSet.getString("newDay");

                resultSet = statement.executeQuery("select TIMESTAMPDIFF(DAY, " +
                        "'" + startDate + "', '" + endDate + "') as different");
                resultSet.next();
                dayDifference = resultSet.getInt("different");

                if(parameter != 1){
                    resultSet = statement.executeQuery("select '" + startDate + "'" +
                            "+ INTERVAL " + ((parameter-2) * 7) + " DAY as newDay");
                    resultSet.next();
                    startDate = resultSet.getString("newDay");
                }

                if(checkDayDifference(startDate,endDate) == false){
                    break;
                }

        }

        this.closeConnection();
        return result;
    }

    public boolean checkDayDifference(String start, String end){
        try {
            resultSet = statement.executeQuery("select TIMESTAMPDIFF(DAY, " +
                    "'" + start + "', '" + end + "') as different");
            resultSet.next();

            if(resultSet.getInt("different") < 1){
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public boolean openMonthlyAppointment(String startDate, String endDate,
                                       String length, String lecturerID) throws SQLException {
        boolean result = true;
        this.connect();
        this.createStatement();
        Integer count = 0;
        String tmp = "";

        while (true){

            resultSet = statement.executeQuery("SELECT ('" + startDate +"' " +
                    " + INTERVAL " + count + " MONTH) as startdate");
            resultSet.next();
            tmp = resultSet.getString("startdate");

            if(checkAppointment(tmp, lecturerID) == false){
                return false;
            }

            statement.executeUpdate("insert into Appointments (LecturerID," +
                    "AppointmentStatus, StartDateTime, Length, StudentID) " +
                    "values(" + lecturerID + ", 1, (SELECT ('" + startDate +"' " +
                    " + INTERVAL " + count + " MONTH) as startdate)" + "," +
                    length + ",0)");

            count = count + 1;

            resultSet = statement.executeQuery("select TIMESTAMPDIFF(DAY, " +
                    "(SELECT ('" + startDate +"' " +
                    " + INTERVAL " + count + " MONTH))" +", '" + endDate +"') as diff\n");
            resultSet.next();
            if(resultSet.getInt("diff") < 1){
                break;
            }

        }

        this.closeConnection();
        return result;
    }

    public boolean checkAppointment(String startDate, String lecturerID){
        try{

            resultSet = statement.executeQuery("select count(*) as count " +
                    "from Appointments where LecturerID = " + lecturerID +
            " and StartDateTime = '" + startDate + "'");
            resultSet.next();

            if (resultSet.getInt("count") > 0){
                return false;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return true;
    }
}
