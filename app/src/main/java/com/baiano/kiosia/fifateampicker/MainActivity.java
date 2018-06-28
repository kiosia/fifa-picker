package com.baiano.kiosia.fifateampicker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private static final String[] TYPES = {"International", "Women", "Regular", "World Cup"};
    private static final String[] TYPES_TAG = {"int", "wmn", "reg", "wc"};
    private static int selectedStars = -1;
    private static int selectedType = -1;
    private TeamsData teams;
    private EasterEggs easterEggs;
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

        easterEggs = new EasterEggs(getApplicationContext());
        teams = new TeamsData(getApplicationContext());
        rollTeams(teams, selectedType, selectedStars);
        rollPlayers();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                rollTeams(teams, selectedType, selectedStars);
                rollPlayers();
            }
        });

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

        fillLabels(rngType, rngStars, pickedTeams);
    }

    private void fillLabels(int rngType, int rngStars, ArrayList<Team> pickedTeams) {
        Team homeTeam = pickedTeams.get(0);
        setHomeTeamLabels(homeTeam, rngType);

        Team awayTeam = pickedTeams.get(1);
        setAwayTeamLabels(awayTeam, rngType);

        fillEasterEggs(homeTeam, awayTeam);

        ImageView starsValue = findViewById(R.id.matchRatingValue);

        TextView matchType = findViewById(R.id.matchTypeLabel);
        matchType.setText(TYPES[rngType]);

        int[] starsImages = {R.drawable.stars05, R.drawable.stars10, R.drawable.stars15, R.drawable.stars20, R.drawable.stars25, R.drawable.stars30, R.drawable.stars35, R.drawable.stars40, R.drawable.stars45, R.drawable.stars50};
        starsValue.setImageResource(starsImages[rngStars]);
    }

    private void fillEasterEggs(Team homeTeam, Team awayTeam) {
        TextView flavorLabel = findViewById(R.id.flavorText);
        flavorLabel.setText(easterEggs.execute(homeTeam, awayTeam));
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

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
