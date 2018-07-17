package com.baiano.kiosia.fifateampicker;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
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
    private final String[] TYPES_TAGS = {"INT", "WMN", "REG", "WC"};
    private static final String[] TYPES = {"International", "Women International", "Regular Teams", "World Cup"};

    private static int selectedStars = -1;
    private static int selectedType = -1;
    private static int selectedVersion = -1;
    private static int[] lastShuffledPlayers = {4, 4, 4, 4};
    private Flavors flavors;
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
        flavors = new Flavors(getApplicationContext());
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

        ImageView teamsShuffleButton = findViewById(R.id.teamsShuffleButton);
        teamsShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollTeams();
            }
        });

        ImageView controllersShuffleButton = findViewById(R.id.controllersShuffleButton);
        controllersShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rollPlayers();
            }
        });

        final View teamRatingSelector = findViewById(R.id.teamRatingSelector);
        ImageView ratingImage = findViewById(R.id.star3);
        ratingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamRatingSelector.setVisibility(View.VISIBLE);
            }
        });

        final View teamTypeSelector = findViewById(R.id.teamTypeSelector);
        ImageView typeBackground = findViewById(R.id.matchTypeBackground);
        typeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teamTypeSelector.setVisibility(View.VISIBLE);
            }
        });

        TextView intLabel = teamTypeSelector.findViewById(R.id.intFilterLabel);
        intLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedType = 0;
                selectedVersion = -1;
                teamTypeSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        TextView wmnLabel = teamTypeSelector.findViewById(R.id.wmnFilterLabel);
        wmnLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedType = 1;
                selectedVersion = -1;
                teamTypeSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        TextView regLabel = teamTypeSelector.findViewById(R.id.regFilterLabel);
        regLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedType = 2;
                selectedVersion = -1;
                teamTypeSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        TextView wcLabel = teamTypeSelector.findViewById(R.id.wcFilterLabel);
        wcLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedType = 3;
                selectedVersion = 2;
                teamTypeSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        TextView typeCancelLabel = teamTypeSelector.findViewById(R.id.cancelLabel);
        typeCancelLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedType = -1;
                selectedVersion = -1;
                teamTypeSelector.setVisibility(View.INVISIBLE);
            }
        });

        View halfStarButton = teamRatingSelector.findViewById(R.id.half);
        halfStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 0;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View oneStarButton = teamRatingSelector.findViewById(R.id.one);
        oneStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 1;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View oneHalfStarButton = teamRatingSelector.findViewById(R.id.oneHalf);
        oneHalfStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 2;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View twoStarButton = teamRatingSelector.findViewById(R.id.two);
        twoStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 3;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View twoHalfStarButton = teamRatingSelector.findViewById(R.id.twoHalf);
        twoHalfStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 4;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View threeStarButton = teamRatingSelector.findViewById(R.id.three);
        threeStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 5;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View threeHalfStarButton = teamRatingSelector.findViewById(R.id.threeHalf);
        threeHalfStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 6;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View fourStarButton = teamRatingSelector.findViewById(R.id.four);
        fourStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 7;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View fourHalfStarButton = teamRatingSelector.findViewById(R.id.fourHalf);
        fourHalfStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 8;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });

        View fiveStarButton = teamRatingSelector.findViewById(R.id.five);
        fiveStarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = 9;
                teamRatingSelector.setVisibility(View.INVISIBLE);
                rollTeams();
            }
        });


        TextView ratingCancelLabel = teamRatingSelector.findViewById(R.id.cancelLabel);
        ratingCancelLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedStars = -1;
                teamRatingSelector.setVisibility(View.INVISIBLE);
            }
        });

        progress.dismiss();
    }

    private void rollPlayers() {
        ImageView[] playersHomeTeamImages = { findViewById(R.id.player1team1image), findViewById(R.id.player2team1image), findViewById(R.id.player3team1image), findViewById(R.id.player4team1image) };
        ImageView[] playersAwayTeamImages = { findViewById(R.id.player1team2image), findViewById(R.id.player2team2image), findViewById(R.id.player3team2image), findViewById(R.id.player4team2image) };

        for (ImageView playersImage : playersHomeTeamImages) {
            playersImage.setVisibility(View.INVISIBLE);
        }

        for (ImageView playersImage : playersAwayTeamImages) {
            playersImage.setVisibility(View.INVISIBLE);
        }

        int[] players = {0, 1, 2, 3};
        do {
            shufflePlayers(players);
        } while ((players[0]+players[1] == lastShuffledPlayers[0]+lastShuffledPlayers[1]) || (players[0]+players[1] == lastShuffledPlayers[2]+lastShuffledPlayers[3]));
        lastShuffledPlayers = players;

        playersHomeTeamImages[players[0]].setVisibility(View.VISIBLE);
        playersHomeTeamImages[players[1]].setVisibility(View.VISIBLE);
        playersAwayTeamImages[players[2]].setVisibility(View.VISIBLE);
        playersAwayTeamImages[players[3]].setVisibility(View.VISIBLE);
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
            try {
                do {
                    pickedTeams = rollTeamsNoFilter(version);
                } while (pickedTeams.size() < 2);
            } catch (NoSuchElementException ex) {
                rollTeams();
                return;
            }
        } else if (selectedStars == -1) {
            try {
                do {
                    pickedTeams = rollTeamsByType(version, selectedType);
                } while (pickedTeams.size() < 2);
            } catch (NoSuchElementException ex) {
                rollTeams();
                return;
            }
        } else if (selectedType == -1) {
            try {
                do {
                    pickedTeams = rollTeamsByStars(version, selectedStars);
                } while (pickedTeams.size() < 2);
            } catch (NoSuchElementException ex) {
                rollTeams();
                return;
            }
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
        Team homeTeam = TeamDao.getRandomTeamByStarsAndType(this, version, RATING[stars], TYPES_TAGS[type]);
        return getTeamsBasedOnHomeTeam(homeTeam);
    }

    private Set<Team> rollTeamsByStars(int version, int stars) {
        Team homeTeam = TeamDao.getRandomTeamByStars(this, version, RATING[stars]);
        return getTeamsBasedOnHomeTeam(homeTeam);
    }

    private Set<Team> rollTeamsByType(int version, int type) {
        Team homeTeam = TeamDao.getRandomTeamByType(this, version, TYPES_TAGS[type]);
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

        fillFlavor(homeTeam, awayTeam);

        TextView matchType = findViewById(R.id.matchTypeLabel);
        for (int i=0; i<TYPES_TAGS.length; i++ ) {
            if (TYPES_TAGS[i].equals(homeTeam.getType())) {
                matchType.setText(TYPES[i]);
                break;
            }
        }

        ImageView star1 = findViewById(R.id.star1);
        ImageView star2 = findViewById(R.id.star2);
        ImageView star3 = findViewById(R.id.star3);
        ImageView star4 = findViewById(R.id.star4);
        ImageView star5 = findViewById(R.id.star5);

        int matchRating = homeTeam.getRatingIndex();

        star1.setImageResource(R.drawable.star_half);
        star2.setImageResource(R.drawable.star_empty);
        star3.setImageResource(R.drawable.star_empty);
        star4.setImageResource(R.drawable.star_empty);
        star5.setImageResource(R.drawable.star_empty);

        switch (matchRating) {
            case 0:
                star1.setImageResource(R.drawable.star_half);
                break;
            case 1:
                star1.setImageResource(R.drawable.star_full);
                break;
            case 2:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_half);
                break;
            case 3:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                break;
            case 4:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                star3.setImageResource(R.drawable.star_half);
                break;
            case 5:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                star3.setImageResource(R.drawable.star_full);
                break;
            case 6:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                star3.setImageResource(R.drawable.star_full);
                star4.setImageResource(R.drawable.star_half);
                break;
            case 7:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                star3.setImageResource(R.drawable.star_full);
                star4.setImageResource(R.drawable.star_full);
                break;
            case 8:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                star3.setImageResource(R.drawable.star_full);
                star4.setImageResource(R.drawable.star_full);
                star5.setImageResource(R.drawable.star_half);
                break;
            case 9:
                star1.setImageResource(R.drawable.star_full);
                star2.setImageResource(R.drawable.star_full);
                star3.setImageResource(R.drawable.star_full);
                star4.setImageResource(R.drawable.star_full);
                star5.setImageResource(R.drawable.star_full);
                break;

        }
    }

    private void fillFlavor(Team homeTeam, Team awayTeam) {
        String flavor = flavors.execute(homeTeam, awayTeam);
        TextView flavorLabel = findViewById(R.id.flavorTextLabel);
        flavorLabel.setText(flavor);
    }

    private void setAwayTeamLabels(Team awayTeam) {
        // setting the league/country label
        TextView awayTeamLeagueCountryLabel = findViewById(R.id.awayTeamLeagueCountryLabel);
        String labelString = "";
        if (!"N/A".equals(awayTeam.getLeague())) {
            labelString = awayTeam.getLeague();
        }
        if (!"N/A".equals(awayTeam.getCountry())) {
            labelString = labelString.concat(String.format(" (%s)", awayTeam.getCountry()));
        }
        awayTeamLeagueCountryLabel.setText(labelString.toUpperCase());

        // setting the team name label
        TextView awayTeamNameLabel = findViewById(R.id.awayTeamNameLabel);
        awayTeamNameLabel.setText(String.format("%s", awayTeam.getName()));

        // setting the badge
        ImageView awayTeamBadge = findViewById(R.id.awayTeamBadge);
        awayTeamBadge.setContentDescription(awayTeam.getName());

        try {
            String formattedName = awayTeam.getName().toLowerCase().replaceAll("[ ']", "_");
            String badgeFilename = "badges/" + awayTeam.getType().toLowerCase() + "_" + formattedName + ".png";
            InputStream badgeInputStream = this.getAssets().open(badgeFilename);
            awayTeamBadge.setImageBitmap(BitmapFactory.decodeStream(badgeInputStream));
            badgeInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // setting attack label
        TextView awayTeamAttackValue = findViewById(R.id.awayTeamAttackValue);
        awayTeamAttackValue.setText(String.format("%s", awayTeam.getAttack()));

        // setting midfield label
        TextView awayTeamMidfieldValue = findViewById(R.id.awayTeamMidfieldValue);
        awayTeamMidfieldValue.setText(String.format("%s", awayTeam.getMidfield()));

        // setting defense label
        TextView awayTeamDefenseValue = findViewById(R.id.awayTeamDefenseValue);
        awayTeamDefenseValue.setText(String.format("%s", awayTeam.getDefense()));
    }

    private void setHomeTeamLabels(Team homeTeam) {
        // setting the league/country label
        TextView homeTeamLeagueCountryLabel = findViewById(R.id.homeTeamLeagueCountryLabel);
        String labelString = "";
        if (!"N/A".equals(homeTeam.getLeague())) {
            labelString = homeTeam.getLeague();
        }
        if (!"N/A".equals(homeTeam.getCountry())) {
            labelString = labelString.concat(String.format(" (%s)", homeTeam.getCountry()));
        }
        homeTeamLeagueCountryLabel.setText(labelString.toUpperCase());

        // setting the team name label
        TextView homeTeamNameLabel = findViewById(R.id.homeTeamNameLabel);
        homeTeamNameLabel.setText(String.format("%s", homeTeam.getName()));

        // setting the badge
        ImageView homeTeamBadge = findViewById(R.id.homeTeamBadge);
        homeTeamBadge.setContentDescription(homeTeam.getName());

        try {
            String formattedName = homeTeam.getName().toLowerCase().replaceAll("[ ']", "_");
            String badgeFilename = "badges/" + homeTeam.getType().toLowerCase() + "_" + formattedName + ".png";
            InputStream badgeInputStream = this.getAssets().open(badgeFilename);
            homeTeamBadge.setImageBitmap(BitmapFactory.decodeStream(badgeInputStream));
            badgeInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // setting attack label
        TextView homeTeamAttackValue = findViewById(R.id.homeTeamAttackValue);
        homeTeamAttackValue.setText(String.format("%s", homeTeam.getAttack()));

        // setting midfield label
        TextView homeTeamMidfieldValue = findViewById(R.id.homeTeamMidfieldValue);
        homeTeamMidfieldValue.setText(String.format("%s", homeTeam.getMidfield()));

        // setting defense label
        TextView homeTeamDefenseValue = findViewById(R.id.homeTeamDefenseValue);
        homeTeamDefenseValue.setText(String.format("%s", homeTeam.getDefense()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        hideNavbar();
    }

    public void hideNavbar() {
        if (Build.VERSION.SDK_INT > 15 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }
}
