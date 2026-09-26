
package org.luis.utils;

public class TestDataUtil {

    public static String uniqueEmail() {
        String base = ConfigReader.get("testEmail");
        int at = base.indexOf("@");
        return base.substring(0, at) + "+" + System.currentTimeMillis() + base.substring(at);
    }

    public static String uniqueCedula() {
        String millis = String.valueOf(System.currentTimeMillis());
        return "9" + millis.substring(millis.length() - 9);
    }
}
 