package com.baiano.kiosia.fifateampicker;

import android.content.Context;

import com.baiano.kiosia.fifateampicker.Model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class Flavors {
    private ArrayList<Flavor> flavors;

    Flavors(Context context) {
        ArrayList<Flavor> flavors = new ArrayList<>();
        String filename = "easter_eggs/easter_eggs.json";
        String json;
        try {
            InputStream inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            this.flavors = null;
            return;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jsonArray = obj.getJSONArray("easterEggs");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Flavor flavor = new Flavor();
                flavor.setConditionType(jsonObject.getString("conditionType"));
                flavor.setOperator(jsonObject.getString("operator"));
                flavor.setFactor1(jsonObject.getString("factor1"));
                flavor.setFactor2(jsonObject.getString("factor2"));
                flavor.setResult(jsonObject.getString("result"));
                flavors.add(flavor);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        this.flavors = flavors;
    }

    public String execute(Team homeTeam, Team awayTeam) {
        StringBuilder result = new StringBuilder();
        for (Flavor flavor : this.flavors) {
            String aux = flavor.execute(homeTeam, awayTeam);
            if (!"".equals(aux)) {
                result.append(aux).append('\n');
            }
        }
        return result.toString();
    }
}