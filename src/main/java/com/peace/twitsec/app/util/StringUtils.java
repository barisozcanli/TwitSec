package com.peace.twitsec.app.util;


import java.util.Arrays;
import java.util.List;

public class StringUtils {

    public static String removeDot(String s) {
       return s.replaceAll("[.]","");
    }

    public static List<String> splitByComma(String s) {
        return (ObjectUtils.isNotNull(s) && !s.isEmpty()) ? Arrays.asList(s.split(",")) : null;
    }

    public static String replaceTurkishCharacters(String text) {
        text = text.replaceAll("ç","&#231;");
        text = text.replaceAll("ı","&#305;");
        text = text.replaceAll("ö","&#246;");
        text = text.replaceAll("ü","&#252;");
        text = text.replaceAll("ş","&#351;");
        text = text.replaceAll("ğ","&#287;");
        text = text.replaceAll("Ç","&#199;");
        text = text.replaceAll("İ","&#304;");
        text = text.replaceAll("Ö","&#214;");
        text = text.replaceAll("Ü","&#220;");
        text = text.replaceAll("Ğ","&#286;");
        text = text.replaceAll("Ş","&#350;");
        return text;
    }
}
