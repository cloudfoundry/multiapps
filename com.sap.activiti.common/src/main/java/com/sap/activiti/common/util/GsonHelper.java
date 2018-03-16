package com.sap.activiti.common.util;

import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class GsonHelper {

    private static final String CHARSET_UTF_8 = "UTF-8";

    public static <T> byte[] getAsBinaryJson(Object obj) {
        try {
            return new GsonBuilder().create()
                .toJson(obj)
                .getBytes(CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <V> V getFromStringJson(String stringJson, Class<V> clazz) {
        try {
            return new Gson().fromJson(stringJson, clazz);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static <V> V getFromBinaryJson(byte[] binaryJson, Class<V> clazz) {
        try {
            return new Gson().fromJson(new String(binaryJson, CHARSET_UTF_8), clazz);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
