package com.baiano.kiosia.fifateampicker.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.support.annotation.NonNull;

import com.baiano.kiosia.fifateampicker.DatabaseHelper;
import com.baiano.kiosia.fifateampicker.Model.Team;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class TeamDao {
    private static final String COLUMN_NAMES = "t.id, t.name, t.attack, t.midfield, t.defense, t.overall, t.rating, t.type, l.name AS league, l.country, t.version";

    public static Set<Team> getAllTeamsByVersion(Context context, int version) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
                "WHERE t.version='"+version+"'";
       return executeQuery(context, query);
    }

    public static Team getRandomTeam(Context context, int version) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
                "WHERE t.version='"+version+"' ORDER BY RANDOM() LIMIT 1";
        return executeQuery(context, query).iterator().next();
    }

    public static Set<Team> findTeamsByCountry(Context context, int version, String leagueCountry) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
            "WHERE l.country='"+leagueCountry+"' AND t.version='"+version+"'";
        return executeQuery(context, query);
    }

    public static Set<Team> findRandomTeamsByTypeAndRating(Context context, int version, String teamType, String teamRating) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
            "WHERE t.rating='"+teamRating+"' AND t.type='"+teamType+"' AND t.version='"+version+"' ORDER BY RANDOM() LIMIT 2";
        return executeQuery(context, query);
    }

    public static Set<Team> findRandomTeamsByRatingAndCountry(Context context, int version, String teamRating, String leagueCountry) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
            "WHERE t.rating='"+teamRating+"' AND l.country='"+leagueCountry+"' AND t.version='"+version+"' ODER BY RANDOM() LIMIT 2";
        return executeQuery(context, query);
    }

    public static Set<Team> findRandomTeamsByRatingAndLeague(Context context, int version, String teamRating, String leagueName) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
                "WHERE t.rating='"+teamRating+"' AND l.name='"+leagueName+"' AND t.version='"+version+"' ORDER BY RANDOM() LIMIT 2";
        return executeQuery(context, query);
    }

    public static Team getTeamBasedOn(Context context, Team homeTeam) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
                "WHERE t.id!='"+homeTeam.getId()+"' AND t.league=l.id AND t.version='"+homeTeam.getVersion()+
                "' AND t.rating='"+homeTeam.getRating()+"' AND t.type='"+homeTeam.getType()+"' ORDER BY RANDOM() LIMIT 1";
        return executeQuery(context, query).iterator().next();
    }

    public static Team getRandomTeamByStarsAndType(Context context, int version, String stars, String type) {
        String query = "SELECT " + COLUMN_NAMES + " FROM team AS t INNER JOIN league AS l ON t.league=l.id " +
                "WHERE t.version='" + version + "' AND t.rating='" + stars + "' AND t.type='" + type + "' ORDER BY RANDOM() LIMIT 1";
        return executeQuery(context, query).iterator().next();
    }

    public static Team getRandomTeamByStars(Context context, int version, String stars) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
                "WHERE t.version='"+version+"' AND t.rating='"+stars+"' ORDER BY RANDOM() LIMIT 1";
        return executeQuery(context, query).iterator().next();
    }

    public static Team getRandomTeamByType(Context context, int version, String type) {
        String query = "SELECT "+COLUMN_NAMES+" FROM team AS t INNER JOIN league AS l ON t.league=l.id "+
                "WHERE t.version='"+version+"' AND t.type='"+type+"' ORDER BY RANDOM() LIMIT 1";
        return executeQuery(context, query).iterator().next();
    }

    @NonNull
    private static Set<Team> executeQuery(Context context, String query) {
        Set<Team> result = new HashSet<>();
        Cursor cursor = DatabaseHelper.getInstance(context).executeQuery(query);
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int attackColumn = cursor.getColumnIndex("attack");
        int midfieldColumn = cursor.getColumnIndex("midfield");
        int defenseColumn = cursor.getColumnIndex("defense");
        int overallColumn = cursor.getColumnIndex("overall");
        int ratingColumn = cursor.getColumnIndex("rating");
        int typeColumn = cursor.getColumnIndex("type");
        int leagueColumn = cursor.getColumnIndex("league");
        int countryColumn = cursor.getColumnIndex("country");
        int versionColumn = cursor.getColumnIndex("version");
        cursor.moveToFirst();
        do {
            try {
                Team version = new Team(cursor.getInt(idColumn),
                        cursor.getString(nameColumn),
                        cursor.getInt(attackColumn),
                        cursor.getInt(midfieldColumn),
                        cursor.getInt(defenseColumn),
                        cursor.getInt(overallColumn),
                        cursor.getString(ratingColumn),
                        cursor.getString(typeColumn),
                        cursor.getString(leagueColumn),
                        cursor.getString(countryColumn),
                        cursor.getInt(versionColumn));
                result.add(version);
            } catch (CursorIndexOutOfBoundsException ex) {
                cursor.close();
                return result;
            }
        } while (cursor.moveToNext());
        cursor.close();
        return result;
    }
}
