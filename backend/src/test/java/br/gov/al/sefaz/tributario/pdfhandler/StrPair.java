package br.gov.al.sefaz.tributario.pdfhandler;

import java.util.Map;

public class StrPair implements Map.Entry<String , String> {
    private String key;
    private String value;

    public StrPair(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String value) {
        String old = this.value;
        this.value = value;
        return old;
    }
}
