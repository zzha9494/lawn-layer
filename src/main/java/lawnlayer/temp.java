package lawnlayer;

import processing.data.JSONArray;
import processing.data.JSONObject;

import processing.core.PApplet;

import java.io.File;

public class temp{
    public static void main(String[] args) {
        JSONObject test = PApplet.loadJSONObject(new File("config.json"));
        int lives = test.getInt("lives");

        JSONArray levels = test.getJSONArray("levels");
        JSONObject level1 = levels.getJSONObject(0);
        String path = level1.getString("outlay");


        System.out.println("1");
    }

    public static void read_json(App app) {
        JSONObject test = app.loadJSONObject("config.json");
        System.out.println("1");
    }
}