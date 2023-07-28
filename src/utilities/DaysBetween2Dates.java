package utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;

public class DaysBetween2Dates {

    public long daysBetween2Dates(String date1, String date2) {
        // Định dạng thời gian
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        // Định nghĩa 2 mốc thời gian ban đầu
        Date date01 = Date.valueOf(date1);
        Date date02 = Date.valueOf(date2);

        c1.setTime(date01);
        c2.setTime(date02);

        // Công thức tính số ngày giữa 2 mốc thời gian:
        long noDay = (c2.getTime().getTime() - c1.getTime().getTime()) / (24 * 3600 * 1000);
        return noDay;
    }

}
