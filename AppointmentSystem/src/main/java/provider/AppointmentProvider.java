package provider;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import org.json.JSONArray;
import org.json.JSONObject;
import repository.AppointmentRepository;
import repository.UserRegistrationRepository;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AppointmentProvider {

    public static JSONObject takeAppointment(HttpServletRequest request) throws Exception{
        JSONObject result = new JSONObject();
        AppointmentRepository appointmentRepository = new AppointmentRepository();

        String randevuAlanID       = request.getHeader("randevuAlanID");
        String randevuVerenID      = request.getHeader("randevuVerenID");
        String randevuTarihi       = request.getHeader("randevuTarihi");

        System.out.println(request.getHeader("randevuTarihi"));
        appointmentRepository.checkAppointment(randevuAlanID, randevuVerenID, randevuTarihi);
        if(appointmentRepository.checkAppointment(randevuAlanID, randevuVerenID, randevuTarihi)){
            appointmentRepository.takeAppointment(randevuAlanID, randevuVerenID, randevuTarihi);
            result.put("status", "ok");
        }else {
            result.put("error", "The appointment that you want to take is not suitable!");
            result.put("status", "ok");
        }

        return result;
    }

    public static JSONObject getAppointments(HttpServletRequest request) throws Exception{
        JSONObject result = new JSONObject();
        AppointmentRepository appointmentRepository = new AppointmentRepository();

        HashMap<String, ArrayList<String>> tmp = appointmentRepository.getAllAppointments
                (request.getHeader("startdate"), request.getHeader("enddate"),
                        request.getHeader("id"));

        ArrayList<String> dates = tmp.get("dates");
        ArrayList<String> lengths = tmp.get("lengths");
        ArrayList<String> status = tmp.get("status");

        result.put("appointment", dates);
        result.put("appointmentStatus", status);
        result.put("lengths", lengths);

        return result;
    }

    public static JSONObject openAppointment(HttpServletRequest request) throws Exception{
        JSONObject result = new JSONObject();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        Integer appointmentType;

        appointmentType = request.getIntHeader("parameter");

        result.put("status", 200);
        if(appointmentType == 1){
            String startDate = request.getHeader("startDate");
            String length = request.getHeader("length");
            String lecturerID = request.getHeader("id");

            if(!appointmentRepository.openSingleAppointment(startDate,
                    lecturerID,length)){
                result.put("error", "There is already an appointment for " +
                        "the datetime that you choosed!");
            }

        }else if(appointmentType == 2 || appointmentType == 3 || appointmentType == 4){
            String startDate = request.getHeader("startDate");
            Integer parameter = Integer.parseInt(request.getHeader("parameter"));
            JSONArray days = new JSONArray(request.getHeader("days"));
            String endDate = request.getHeader("endDate");
            String length = request.getHeader("length");
            String lecturerID = request.getHeader("id");

            if(!appointmentRepository.openWeeklyAppointment(startDate, endDate, days,
                    parameter, lecturerID, length)) {
                result.put("error", "There is already an appointment for the one of" +
                        "the datetimes that you choosed!");
            }
        }else if(appointmentType == 5){
            String startDate = request.getHeader("startDate");
            String endDate = request.getHeader("endDate");
            String length = request.getHeader("length");
            String lecturerID = request.getHeader("id");

            if(!appointmentRepository.openMonthlyAppointment(startDate, endDate,
                    length, lecturerID)) {
                result.put("error", "There is already an appointment for the one of" +
                        "the datetimes that you choosed!");
            }
        }

        return result;
    }
}
