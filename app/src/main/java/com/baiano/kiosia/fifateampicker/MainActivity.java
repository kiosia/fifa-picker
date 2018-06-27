package com.baiano.kiosia.fifateampicker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String[] TYPES = {"International", "Women", "Regular", "World Cup"};
    private static final String[] TYPES_TAG = {"int", "wmn", "reg", "wc"};
    private static final String[] STARS = {"Half Star", "One Star", "One and a Half Star", "Two Stars", "Two and a Half Stars", "Three Stars", "Three and a Half Stars", "Four Stars", "Four and a Half Stars", "Five Stars"};
    private static int selectedStars = -1;
    private static int selectedType = -1;
    private TeamsData teams;
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

        teams = new TeamsData(getApplicationContext());
        rollTeams(teams, selectedType, selectedStars);
        rollPlayers();
        progress.dismiss();
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
    private void rollTeams(TeamsData teams, int type, int stars) {
        int rngType = 0;
        int rngStars = 0;
        ArrayList<Team> pickedTeams = new ArrayList<>();

        while (pickedTeams.size() < 2) {
            // this will go up to 3 only so the world cup teams
            // only get picked when the user really wants.
            rngType = rng.nextInt(3);
            rngStars = rng.nextInt(10);
            if (type != -1) {
                rngType = type;
            }
            if (stars != -1) {
                rngStars = stars;
            }
            pickedTeams = teams.getTwoTeamsByTypeAndRating(rngType, rngStars);
        }

        Team homeTeam = pickedTeams.get(0);
        setHomeTeamLabels(homeTeam, rngType);

        Team awayTeam = pickedTeams.get(1);
        setAwayTeamLabels(awayTeam, rngType);

        easterEggs(rngStars, homeTeam, awayTeam);

        ImageView starsValue = findViewById(R.id.matchRatingValue);

        int[] starsImages = {R.drawable.stars05, R.drawable.stars10, R.drawable.stars15, R.drawable.stars20, R.drawable.stars25, R.drawable.stars30, R.drawable.stars35, R.drawable.stars40, R.drawable.stars45, R.drawable.stars50};
        starsValue.setImageResource(starsImages[rngStars]);

        TextView matchType = findViewById(R.id.matchTypeLabel);
        matchType.setText(TYPES[rngType]);
    }

    private void easterEggs(int rngStars, Team homeTeam, Team awayTeam) {
        TextView flavorLabel = findViewById(R.id.flavorText);
        flavorLabel.setText(EasterEggs.getString(homeTeam, awayTeam));

        TextView starsLabel = findViewById(R.id.matchRatingLabel);
        starsLabel.setText(STARS[rngStars]);

        TextView matchType = findViewById(R.id.matchTypeLabel);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Toast toast;
        switch(item.getItemId()) {
            case R.id.rerollPlayers:
                rollPlayers();
                break;
            case R.id.rerollTeams:
                rollTeams(teams, selectedType, selectedStars);
                break;
            case R.id.starsFilter:
                toast = Toast.makeText(getApplicationContext(), "Filtering by Stars soon...",Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.resetFilters:
                selectedStars = -1;
                selectedType = -1;
                toast = Toast.makeText(getApplicationContext(), "Cleared filters!",Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.typeFilter:
                builder.setTitle("Choose the team's type");
                builder.setItems(TYPES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedType = i;
                        rollTeams(teams, selectedType, selectedStars);
                    }
                });
                builder.show();
                break;
            case R.id.options:
                // open options
                toast = Toast.makeText(getApplicationContext(), "Options soon...",Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.about:
                // open about modal
                toast = Toast.makeText(getApplicationContext(), "About soon...",Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
