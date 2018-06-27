package com.baiano.kiosia.fifateampicker;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

class TeamsData {
    private final TeamsByRating internationalTeams;
    private final TeamsByRating internationalWomenTeams;
    private final TeamsByRating regularTeams;
    private final TeamsByRating worldCupTeams;

    public TeamsData(Context context) {
        String FIFA_GAME = "fifa18_";
        String FIFA_VERSION = "174_";
        String INTERNATIONAL = "int";
        String INTERNATIONAL_WOMEN = "int_wmn";
        String REGULAR = "reg";
        String WORLD_CUP = "wc";

        this.internationalTeams = loadTeamsByType(context, FIFA_GAME+FIFA_VERSION+INTERNATIONAL);
        this.internationalWomenTeams = loadTeamsByType(context, FIFA_GAME+FIFA_VERSION+INTERNATIONAL_WOMEN);
        this.regularTeams = loadTeamsByType(context, FIFA_GAME+FIFA_VERSION+REGULAR);
        this.worldCupTeams = loadTeamsByType(context, FIFA_GAME+WORLD_CUP);
    }

    public TeamsByRating getInternationalTeams() {
        return internationalTeams;
    };

    public TeamsByRating getInternationalWomenTeams() {
        return internationalWomenTeams;
    };

    public TeamsByRating getRegularTeams() {
        return regularTeams;
    }

    public TeamsByRating getWorldCupTeams() {
        return worldCupTeams;
    }

    private TeamsByRating loadTeamsByType(Context context, String type) {
        TeamsByRating result = new TeamsByRating();

        result.setHalfStarTeams(loadTeamsByTypeAndRating(context, type, "05"));
        result.setOneStarTeams(loadTeamsByTypeAndRating(context, type, "10"));
        result.setOneHalfStarTeams(loadTeamsByTypeAndRating(context, type, "15"));
        result.setTwoStarsTeams(loadTeamsByTypeAndRating(context, type, "20"));
        result.setTwoHalfStarsTeam(loadTeamsByTypeAndRating(context, type, "25"));
        result.setThreeStarsTeam(loadTeamsByTypeAndRating(context, type, "30"));
        result.setThreeHalfStarsTeam(loadTeamsByTypeAndRating(context, type, "35"));
        result.setFourStarsTeam(loadTeamsByTypeAndRating(context, type, "40"));
        result.setFourHalfStarsTeam(loadTeamsByTypeAndRating(context, type, "45"));
        result.setFiveStarsTeam(loadTeamsByTypeAndRating(context, type, "50"));

        return result;
    }

    private ArrayList<Team> loadTeamsByTypeAndRating(Context context, String teamType, String teamRating) {
        ArrayList<Team> teams = new ArrayList<>();
        String filename = teamType + "_" + teamRating + "_stats.json";
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
            return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray m_jArry = obj.getJSONArray("teams");

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jsonObject = m_jArry.getJSONObject(i);
                Team team = new Team();
                team.setName(jsonObject.getString("name"));
                team.setAttack(jsonObject.getString("attack"));
                team.setMidfield(jsonObject.getString("midfield"));
                team.setDefense(jsonObject.getString("defense"));
                team.setOverall(jsonObject.getString("overall"));
                team.setRating(jsonObject.getString("rating"));
                if (teamType != "fifa18_174_reg") {
                    team.setLeague(jsonObject.getString("league"));
                    team.setCountry(jsonObject.getString("country"));
                }
                team.setCrestId(jsonObject.getString("crest_id"));

                teams.add(team);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return teams;
    }

    public ArrayList<Team> getTwoTeamsByTypeAndRating(int type, int stars) {
        switch (type) {
            case 0 :
                return getTwoTeamsFrom(getTeamsByStars(getInternationalTeams(), stars));
            case 1:
                return getTwoTeamsFrom(getTeamsByStars(getInternationalWomenTeams(), stars));
            case 2:
                return getTwoTeamsFrom(getTeamsByStars(getRegularTeams(), stars));
            case 3:
                return getTwoTeamsFrom(getTeamsByStars(getWorldCupTeams(), stars));
            default:
                return getTwoTeamsFrom(getTeamsByStars(getRegularTeams(), stars));
        }
    }

    private ArrayList<Team> getTwoTeamsFrom(ArrayList<Team> teams) {
        ArrayList<Team> result = new ArrayList<>();
        int teamCount = teams.size();
        if (teamCount > 1) {
            final Random r = new Random();
            int team1, team2;
            do {
                team1 = r.nextInt(teamCount);
                team2 = r.nextInt(teamCount);
            } while(team1 == team2);
            result.add(teams.get(team1));
            result.add(teams.get(team2));
        }
        return result;
    }

    private ArrayList<Team> getTeamsByStars(TeamsByRating teams, int stars) {
        switch (stars) {
            case 0 :
                return teams.getHalfStarTeams();
            case 1 :
                return teams.getOneStarTeams();
            case 2 :
                return teams.getOneHalfStarTeams();
            case 3 :
                return teams.getTwoStarsTeams();
            case 4 :
                return teams.getTwoHalfStarsTeam();
            case 5 :
                return teams.getThreeStarsTeam();
            case 6 :
                return teams.getThreeHalfStarsTeam();
            case 7 :
                return teams.getFourStarsTeam();
            case 8 :
                return teams.getFourHalfStarsTeam();
            case 9 :
                return teams.getFiveStarsTeam();
            default:
                return teams.getFiveStarsTeam();
        }
    }
}
