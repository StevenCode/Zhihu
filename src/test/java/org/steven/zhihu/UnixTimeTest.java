package org.steven.zhihu;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UnixTimeTest {
    @Test
    public void testUnixTime() {


        long l = System.currentTimeMillis()/1000;
        System.out.println(l);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar instance = Calendar.getInstance();
        String format = sdf.format(new Date((1530536872*1000)));
        System.out.println("format: "+format);
        try {
            Date parse = sdf.parse("20180704");

            instance.setTime(parse);
            long current = instance.getTimeInMillis() / 1000;

            parse = sdf.parse("20180703");

            instance.setTime(parse);
            long last = instance.getTimeInMillis() / 1000;

            long l1 = current - last;
            System.out.println(l1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
