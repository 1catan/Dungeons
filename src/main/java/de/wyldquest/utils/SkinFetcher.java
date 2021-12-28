package de.wyldquest.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SkinFetcher {

    private Gson gson;

    public String getUUIDasString(String name) {
        try {
            URLConnection connection = new URL("https://api.mojang.com/users/profiles/minecraft/"+name).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            gson = new Gson();
            JsonObject jsonObject = gson.fromJson(line, JsonObject.class);
            String uuid = jsonObject.get("id").getAsString();
            return uuid;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String[] getSkin(String uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray jsonArray = jsonObject.get("properties").getAsJsonArray();
            JsonObject jsonObject1 = jsonArray.get(0).getAsJsonObject();
            String value = jsonObject1.get("value").getAsString();
            String signature = jsonObject1.get("signature").getAsString();
            return new String[]{value, signature, uuid};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
