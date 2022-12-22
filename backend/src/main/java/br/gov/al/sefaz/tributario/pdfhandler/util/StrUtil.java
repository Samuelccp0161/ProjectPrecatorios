package br.gov.al.sefaz.tributario.pdfhandler.util;

public class StrUtil {
    private StrUtil() {}

    public static String truncar(String str, int len) {
        if (str.length() <= len) return str;

        return str.substring(0, len - 2) + "..";
    }

    public static String stripAndRemove(String valor, String regex) {
        return valor.replaceAll(regex, "").strip();
    }

    public static String lastWord(String row) {
        var words = row.split(" ");

        return (words.length > 0)? words[words.length - 1] : "";
    }
}
