# appointment-management-system
Appointment Management System For University Departments
API List

* Bütün parametreler request header'a sağ taraftaki isimler ile gelmelidir. Dönüşler JSON formatındadır.
** Tarih her API için klasik DateTime formatında olmalı: 2011-01-22 00:00:00

--> http://13.59.53.130:8080/AppointmentSystem/rest/user/login 
Parametreler:username, password veya yalnızca token
Dönüş: status, token, error, userid (error varsa error mesajı döner, sadece status 202'mi bakmanızda yeterli, 
Eğer token ile giriş yapılmış ise yalnızca status döner.)

--> http://13.59.53.130:8080/AppointmentSystem/rest/user/register 
Parametreler: username, password, email, usertype, email, tel, gender
Dönüş: status, error(token valid değilse, yine error key değerine sahip bir value var mı kontrolü yeterli)

--> http://13.59.53.130:8080/AppointmentSystem/rest/appointment/take (Randevu Alma)
Parametreler: randevuAlanID, randevuVerenID, randevuTarihi(Başlangıç) 
Dönüş: status

--> http://13.59.53.130:8080/AppointmentSystem/rest/appointment/getAppointments (Bir randevu veren
için girilen tarih aralığındaki randevuları çekme.)
Parametreler: startdate, endate, id
Dönüş:dates(ArrayList), lengths(ArrayList), appointmentStatus(ArrayList) (Aynı gözdeki elemanlar aynı randevuya ait bilgiler demek)
Örnek Dönüş:{"lengths":["123","123","123"], "appointmentStatus":["1","1","1"], "appointment":["2011-01-22 00:00:00.0","2011-02-22 00:00:00.0","2011-03-22 00:00:00.0"]}

--> http://13.59.53.130:8080/AppointmentSystem/rest/appointment/openAppointment (Randevu Açma, 5 farklı şekilde randevu açılabilir, parametreler ve değerleri:
1 - Tekli randevu açma, girilen startdate tarihinde 1 randevu açar.
2 - Haftalık randevu açma, girilen haftanın günleri için enddate tarihine kadar her hafta randevu açar.
3 - Bir üsttekinin aynısını 2 haftada bir yapar.
4 - Bir üsttekinin aynısını 3 haftada bir yapar.
5 - Aylık bazda randevu açma, girilen gün değeri için(Her ay randevu açar. Eğer ilgili ay için girilen gün değeri yoksa örnek olarak 30 Şubat gibi, bunu 28 Şubat olarak günceller. Başlangıç değerine, startDate, ait gün değeri burada randevu açılacak güne 
denk gelmektedir.)
Parametreler: startDate, endDate, id(randevu açan kişinin), length, parameter(Yukarıda bahsedilen parametre), days(JSONArray formatındadır. Haftalık bazda randevu açılacak ise bu parametre gerekli. Örnek input: [{"day":"1"},{"day":"2"}])
Dönüş: status, error
