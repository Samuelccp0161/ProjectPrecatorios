package br.gov.al.sefaz.tributario.pdfhandler.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrUtil {
    private StrUtil() {}

    public static List<String> getNumeros(String str) {
        Matcher num = Pattern.compile("\\d+").matcher(str);
        List<String> numeros = new ArrayList<>();

        while (num.find())
            numeros.add(num.group());

        return numeros;
    }

    public static List<String> getFloats(String str, String separador) {
        Matcher num = Pattern.compile("\\d+" + separador + "\\d+").matcher(str);
        List<String> numeros = new ArrayList<>();

        while (num.find())
            numeros.add(num.group());

        return numeros;
    }

    public static String truncar(String str, int len) {
        if (str.length() <= len) return str;

        return str.substring(0, len - 2) + "..";
    }

    public static String stripAndRemove(String valor, String regex) {
        return valor.replaceAll(regex, "").strip();
    }
}
