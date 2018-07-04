package org.steven.zhihu;

import org.junit.Test;
import org.steven.zhihu.util.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    @Test
    public void testGetAfterId() {
        String url = "https://www.zhihu.com/api/v4/members/excited-vczh/activities?limit=7&after_id=1530007759" +
                "&desktop=True";
        Pattern pattern = Pattern.compile("^http.*after_id=(\\d+).*");
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            String group = matcher.group(1);
            System.out.println(group);
        }

        String format = String.format(Constants.DETAIL_URL, "1530633600");
        System.out.println(format);

    }


}
