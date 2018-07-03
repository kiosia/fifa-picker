package com.baiano.kiosia.fifateampicker.Dao;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.baiano.kiosia.fifateampicker.DatabaseHelper;
import com.baiano.kiosia.fifateampicker.Model.League;

import java.util.ArrayList;
import java.util.List;

public class LeagueDao {

    public static List<League> findAllLeagues(Context context) {
        String query = "SELECT * FROM league";
        return executeQuery(context, query);
    }

   public static List<League> findAllLeaguesByName(Context context, String name) {
       String query = "SELECT * FROM league WHERE name='"+name+"'";
       return executeQuery(context, query);
   }

    public static List<League> findAllLeaguesByCountry(Context context, String country) {
        String query = "SELECT * FROM league WHERE country='"+country+"'";
        return executeQuery(context, query);
    }

    @NonNull
    private static List<League> executeQuery(Context context, String query) {
        List<League> result = new ArrayList<>();
        Cursor cursor = DatabaseHelper.getInstance(context).executeQuery(query);
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int countryColumn = cursor.getColumnIndex("country");
        cursor.moveToFirst();
        do {
            League league = new League(cursor.getInt(idColumn), cursor.getString(nameColumn), cursor.getColumnName(countryColumn));
            result.add(league);
        } while (cursor.moveToNext());
        cursor.close();
        return result;
    }
}
