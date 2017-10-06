package com.sap.activiti.common.util;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class PasswordMasker {
    private static final String MASK = "***";
    private static final String[] defaultPasswordNameFragments = { "pwd", "pass" };

    private String[] passwordFragments;

    public PasswordMasker() {
        passwordFragments = defaultPasswordNameFragments;
    }

    public PasswordMasker(String[] passwordWordFragments) {
        this.passwordFragments = passwordWordFragments;
    }

    public String maskPasswordsFromJson(String jsonString) {
        if (jsonString == null || "".equals(jsonString)) {
            return jsonString;
        }

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        maskPasswordsInJsonStructure(jsonElement);

        return jsonElement.toString();
    }

    private void maskPasswordsInJsonStructure(JsonElement jsonElement) {
        if (jsonElement.isJsonArray()) {
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                maskPasswordsInJsonStructure(element);
            }
        } else if (jsonElement.isJsonObject()) {
            for (Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                if (entry.getValue().isJsonPrimitive()) {
                    handlePrimitive(entry);
                } else {
                    maskPasswordsInJsonStructure(entry.getValue());
                }
            }
        }
    }

    private void handlePrimitive(Entry<String, JsonElement> entry) {
        if (!((JsonPrimitive) entry.getValue()).isString()) {
            return;
        }
        for (int i = 0; i < passwordFragments.length; i++) {
            if (entry.getKey().contains(passwordFragments[i])) {
                entry.setValue(new JsonPrimitive(MASK));
                break;
            }
        }
    }
}
