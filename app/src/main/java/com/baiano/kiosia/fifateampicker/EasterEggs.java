package com.baiano.kiosia.fifateampicker;

import android.content.Context;

import com.baiano.kiosia.fifateampicker.Model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class EasterEggs {
    private ArrayList<EasterEgg> easterEggs;

    EasterEggs(Context context) {
        ArrayList<EasterEgg> easterEggs = new ArrayList<>();
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
            this.easterEggs = null;
            return;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("easterEggs");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jsonObject = m_jArry.getJSONObject(i);
                EasterEgg easterEgg = new EasterEgg();
                easterEgg.setConditionType(jsonObject.getString("conditionType"));
                easterEgg.setOperator(jsonObject.getString("operator"));
                easterEgg.setFactor1(jsonObject.getString("factor1"));
                easterEgg.setFactor2(jsonObject.getString("factor2"));
                easterEgg.setResult(jsonObject.getString("result"));
                easterEggs.add(easterEgg);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        this.easterEggs = easterEggs;
    }

    public String execute(Team homeTeam, Team awayTeam) {
        StringBuilder result = new StringBuilder();
        for (EasterEgg easterEgg : this.easterEggs) {
            String aux = easterEgg.execute(homeTeam, awayTeam);
            if (!"".equals(aux)) {
                result.append(aux).append('\n');
            }
        }
        return result.toString();
    }
}