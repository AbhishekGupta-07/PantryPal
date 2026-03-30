package com.example.pantrypal.utils;

import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GeminiHelper {

    private static final String API_KEY = "PASTE_YOUR_API_KEY_HERE";

    public static String generateResponse(String prompt) {

        OkHttpClient client = new OkHttpClient();

        try {

            JSONObject json = new JSONObject();
            JSONArray contents = new JSONArray();

            JSONObject content = new JSONObject();
            JSONArray parts = new JSONArray();

            JSONObject part = new JSONObject();
            part.put("text", prompt);

            parts.put(part);
            content.put("parts", parts);
            contents.put(content);

            json.put("contents", contents);

            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful() || response.body() == null) {
                return "API Error ❌";
            }

            String res = response.body().string();

            JSONObject obj = new JSONObject(res);

            return obj.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

        } catch (Exception e) {
            e.printStackTrace();
            return "Parsing failed ❌";
        }
    }
}