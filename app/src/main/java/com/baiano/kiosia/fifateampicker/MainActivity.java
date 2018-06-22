package com.baiano.kiosia.fifateampicker;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] TYPES = {"International", "Women", "Regular", "World Cup"};
    private static final String[] TYPES_TAG = {"int", "wmn", "reg", "wc"};
    private static final String[] STARS = {"Half Star", "One Star", "One and a Half Star", "Two Stars", "Two and a Half Stars", "Three Stars", "Three and a Half Stars", "Four Stars", "Four and a Half Stars", "Five Stars"};
    final Random rng = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Reticulating splines...");
        progress.setCancelable(false);
        progress.show();
        final TeamsData teams = new TeamsData(getApplicationContext());
        rollTeams(teams, -1);
        rollPlayers();
        progress.dismiss();

        final Button rollPlayers = findViewById(R.id.rerollPlayers);
        rollPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollPlayers();
            }
        });

        final Button rollTeams = findViewById(R.id.rerollTeams);
        rollTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollTeams(teams, -1);
            }
        });

        final Button rollTeamsInternational = findViewById(R.id.intFilter);
        rollTeamsInternational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollTeams(teams, 0);
            }
        });

        final Button rollTeamsWomen = findViewById(R.id.wmnFilter);
        rollTeamsWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollTeams(teams, 1);
            }
        });

        final Button rollTeamsRegular = findViewById(R.id.regFilter);
        rollTeamsRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollTeams(teams, 2);
            }
        });

        final Button rollTeamsWorldCup = findViewById(R.id.wcFilter);
        rollTeamsWorldCup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollTeams(teams, 3);
            }
        });
    }

    private void rollPlayers() {
        ImageView player1img = findViewById(R.id.player1controller);
        ImageView player2img = findViewById(R.id.player2controller);
        ImageView player3img = findViewById(R.id.player3controller);
        ImageView player4img = findViewById(R.id.player4controller);

        int[] players = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4};
        shufflePlayers(players);

        player1img.setImageResource(players[0]);
        player2img.setImageResource(players[1]);
        player3img.setImageResource(players[2]);
        player4img.setImageResource(players[3]);
    }

    private void shufflePlayers(int[] players) {
        for (int i=3; i>0; i--) {
            int index = rng.nextInt(i + 1);
            int a = players[index];
            players[index] = players[i];
            players[i] = a;
        }
    }

    // sending type -1 will roll a random type
    private void rollTeams(TeamsData teams, int type) {
        int rngType = 0;
        int rngStars = 0;
        ArrayList<Team> pickedTeams = new ArrayList<>();

        while (pickedTeams.size() < 2) {
            rngType = rng.nextInt(4);
            rngStars = rng.nextInt(10);
            if (type != -1) {
                rngType = type;
            }
            pickedTeams = teams.getTwoTeamsByTypeAndRating(rngType, rngStars);
        }

        Team homeTeam = pickedTeams.get(0);
        setHomeTeamLabels(homeTeam, rngType);

        Team awayTeam = pickedTeams.get(1);
        setAwayTeamLabels(awayTeam, rngType);

        TextView starsLabel = findViewById(R.id.gameRatingLabel);
        starsLabel.setText(STARS[rngStars]);

        TextView matchType = findViewById(R.id.teamsTypeLabel);
        matchType.setText(TYPES[rngType]);
    }

    private void setAwayTeamLabels(Team awayTeam, int rngType) {
        TeamView awayTeamView = findViewById(R.id.awayTeam);
        awayTeamView.setTeam(awayTeam, TYPES_TAG[rngType]);
    }

    private void setHomeTeamLabels(Team homeTeam, int rngType) {
        TeamView homeTeamView = findViewById(R.id.homeTeam);
        homeTeamView.setTeam(homeTeam, TYPES_TAG[rngType]);
    }

}