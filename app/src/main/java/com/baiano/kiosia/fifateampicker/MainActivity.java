package com.baiano.kiosia.fifateampicker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import com.baiano.kiosia.fifateampicker.Dao.TeamDao;
import com.baiano.kiosia.fifateampicker.Model.Team;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private final String[] RATING = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50"};
    private final String[] TYPE = {"INT", "WMN", "REG", "WC"};
    private static final String[] TYPES = {"International", "Women", "Regular", "World Cup"};

    private static int selectedStars = -1;
    private static int selectedType = -1;
    private static int selectedVersion = -1;
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
        rollTeams();
        rollPlayers();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if ( mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mShakeDetector = new ShakeDetector();
            mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
                @Override
                public void onShake(int count) {
                    rollTeams();
                    rollPlayers();
                }
            });
        }
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

    private void rollTeams() {
        Set<Team> pickedTeams;
        Integer version = selectedVersion;
        if (version == -1) {
            // default version is 1
            // which should be the current
            version = 1;
        }
        if ((selectedType == -1) && (selectedStars == -1)) {
            do {
                pickedTeams = rollTeamsNoFilter(version);
            } while (pickedTeams.size() < 2);
        } else if (selectedStars == -1) {
            do {
                pickedTeams = rollTeamsByType(version, selectedType);
            } while (pickedTeams.size() < 2);
        } else if (selectedType == -1) {
            do {
                pickedTeams = rollTeamsByStars(version, selectedStars);
            } while (pickedTeams.size() < 2);
        } else {
            try {
                pickedTeams = rollTeamsByStarsAndType(version, selectedType, selectedStars);
            } catch (NoSuchElementException ex) {
                Toast toast = Toast.makeText(getApplicationContext(), "No teams matching your filters. Reset or review them please.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        }
        fillLabels(pickedTeams);
    }

    private Set<Team> rollTeamsByStarsAndType(int version, int type, int stars) {
        Team homeTeam = TeamDao.getRandomTeamByStarsAndType(this, version, RATING[stars], TYPE[type]);
        return getTeamsBasedOnHomeTeam(homeTeam);
    }

    private Set<Team> rollTeamsByStars(int version, int stars) {
        Team homeTeam = TeamDao.getRandomTeamByStars(this, version, RATING[stars]);
        return getTeamsBasedOnHomeTeam(homeTeam);
    }

    private Set<Team> rollTeamsByType(int version, int type) {
        Team homeTeam = TeamDao.getRandomTeamByType(this, version, TYPE[type]);
        return getTeamsBasedOnHomeTeam(homeTeam);
    }

    private Set<Team> rollTeamsNoFilter(int version) {
        Team homeTeam = TeamDao.getRandomTeam(this, version);
        return getTeamsBasedOnHomeTeam(homeTeam);
    }

    @NonNull
    private Set<Team> getTeamsBasedOnHomeTeam(Team homeTeam) {
        Team awayTeam = TeamDao.getTeamBasedOn(this, homeTeam);
        Set<Team> result = new HashSet<>();
        result.add(homeTeam);
        result.add(awayTeam);
        return result;
    }

    private void fillLabels(Set<Team> pickedTeams) {
        Iterator<Team> it = pickedTeams.iterator();
        Team homeTeam = it.next();
        setHomeTeamLabels(homeTeam);

        Team awayTeam = it.next();
        setAwayTeamLabels(awayTeam);

        fillEasterEggs(homeTeam, awayTeam);

        TextView matchType = findViewById(R.id.matchTypeLabel);
        matchType.setText(homeTeam.getType());

        ImageView starsValue = findViewById(R.id.matchRatingValue);
        int[] starsImages = {R.drawable.stars05, R.drawable.stars10, R.drawable.stars15, R.drawable.stars20, R.drawable.stars25, R.drawable.stars30, R.drawable.stars35, R.drawable.stars40, R.drawable.stars45, R.drawable.stars50};
        starsValue.setImageResource(starsImages[homeTeam.getRatingIndex()]);
    }

    private void fillEasterEggs(Team homeTeam, Team awayTeam) {
        TextView flavorLabel = findViewById(R.id.flavorText);
        flavorLabel.setText(easterEggs.execute(homeTeam, awayTeam));
    }

    private void setAwayTeamLabels(Team awayTeam) {
        TeamView awayTeamView = findViewById(R.id.awayTeam);
        awayTeamView.setTeam(awayTeam);
    }

    private void setHomeTeamLabels(Team homeTeam) {
        TeamView homeTeamView = findViewById(R.id.homeTeam);
        homeTeamView.setTeam(homeTeam);
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
                rollTeams();
                break;
            case R.id.starsFilter:
                builder.setTitle("Choose the team's stars");
                builder.setItems(RATING, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedStars = i;
                        rollTeams();
                    }
                });
                builder.show();
                break;
            case R.id.resetFilters:
                selectedStars = -1;
                selectedType = -1;
                selectedVersion = -1;
                toast = Toast.makeText(getApplicationContext(), "Cleared filters!", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.typeFilter:
                builder.setTitle("Choose the team's type");
                builder.setItems(TYPES, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedType = i;
                        if (selectedType == 3) {
                            selectedVersion = 2;
                        } else {
                            selectedVersion = -1;
                        }
                        rollTeams();
                    }
                });
                builder.show();
                break;
            case R.id.options:
                // open options
                toast = Toast.makeText(getApplicationContext(), "Options soon...", Toast.LENGTH_SHORT);
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
