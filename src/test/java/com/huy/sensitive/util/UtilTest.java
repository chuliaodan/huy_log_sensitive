package com.huy.sensitive.util;

import com.huy.sensitive.utils.Util;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: huyong
 * @since: 2020/2/5 23:49
 */
public class UtilTest {

    @Test
    public void replace() {

        System.out.println(Util.replace("王小二", 1, 1, "*"));
        System.out.println(Util.replace("王小二", 1, 5, "*"));
        System.out.println(Util.replace("18500982341", 4, 4, "*"));
        System.out.println(Util.replace("18500982341", 4, 40, "*"));

        String val = "15198782341";
        System.out.println(Util.replace(val, val.length() - 7 + 1, 3, "*"));

    }

    @Test
    public void isPrimitive() {
        System.out.println(Util.isPrimitive(1));
        System.out.println(Util.isPrimitive(1.0f));
        System.out.println(Util.isPrimitive(1.0d));
        System.out.println(Util.isPrimitive(100L));
        System.out.println(Util.isPrimitive(new Integer(10)));
        System.out.println(Util.isPrimitive(new Long(10)));
        System.out.println(Util.isPrimitive(new BigDecimal(10.01)));
        System.out.println(Util.isPrimitive(new BigInteger("30")));
        System.out.println(Util.isPrimitive("huy"));
        System.out.println(Util.isPrimitive('a'));
        System.out.println("==========================================");
        System.out.println(Util.isPrimitive(true));
    }

    @Test
    public void testRegex() {
        Pattern pattern = Pattern.compile("^(\\d{7,8})|(0\\d{2}-?\\d{8})|(0\\d{3}-?\\d{7,8})$");
        Matcher matcher = pattern.matcher("0851-42276541");
        System.out.println(matcher.find());
    }

}