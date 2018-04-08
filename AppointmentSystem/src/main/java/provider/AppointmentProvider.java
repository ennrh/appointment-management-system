package provider;

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
                        request.getHeader("lecturerID"));

        ArrayList<String> dates = tmp.get("dates");
        ArrayList<String> lengths = tmp.get("lengths");

        for(int i = 0; i<dates.size(); i++){
            result.put(dates.get(i), lengths.get(i));
        }

        return result;
    }
}
