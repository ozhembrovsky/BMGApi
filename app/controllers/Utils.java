package controllers;

import play.Logger;
import java.util.Map;

public class Utils {
	public static String[] parameters(Map<String, String[]> values, String... names) { //values = request().body().asFormUrlEncoded()
        if (values == null || names == null || names.length == 0)
            return null;

        String[] result = new String[names.length];
        int i = 0;

        for(Map.Entry<String, String[]> e : values.entrySet()) {
            if (names[i].equals(e.getKey())) {
                String[] val = e.getValue();

                if (val.length > 1)
                    Logger.info("Number of values from request param " + names + " " + val.length);

                result[i++] = val[0];
            }
        }

        return result;
    }
}