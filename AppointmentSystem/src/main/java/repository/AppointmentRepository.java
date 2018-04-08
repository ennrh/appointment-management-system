package repository;

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

        ArrayList<String> dates = new ArrayList<String>();
        ArrayList<String> lengths = new ArrayList<String>();

        resultSet = this.statement.executeQuery("select StartDateTime as startdatetime," +
                " Length as length from Appointments where " +
                "StartDateTime between '" + startDate + "' and '" + endDate + "' and " +
                "LecturerID = '" + randevuVerenID + "'");

        while (resultSet.next()){
            dates.add(resultSet.getString("startdatetime"));
            lengths.add(resultSet.getString("length"));
        }

        HashMap<String, ArrayList<String>> tmp =
                new HashMap<>();

        tmp.put("lengths",lengths);
        tmp.put("dates", dates);

        this.closeConnection();
        return tmp;
    }
}
