package com.baiano.kiosia.fifateampicker;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class TeamView extends LinearLayout {

    private ImageView teamBadge;
    private TextView teamNameLabel;
    private TextView teamAttackLabel;
    private TextView teamMidfieldLabel;
    private TextView teamDefenseLabel;
    private TextView teamOverallLabel;
    private TextView teamLeagueLabel;
    private TextView teamCountryLabel;

    public TeamView(Context context) {
        super(context);
        init();
    }

    public TeamView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TeamView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TeamView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_team, this);
        teamBadge = findViewById(R.id.teamBadge);
        teamNameLabel = findViewById(R.id.teamNameLabel);
        teamAttackLabel = findViewById(R.id.teamAttackLabel);
        teamMidfieldLabel = findViewById(R.id.teamMidfieldLabel);
        teamDefenseLabel = findViewById(R.id.teamDefenseLabel);
        teamLeagueLabel = findViewById(R.id.teamLeagueLabel);
        teamCountryLabel = findViewById(R.id.teamCountryLabel);
    }

    public void setTeam(Team teamData, String teamType) {
        teamBadge.setContentDescription(teamData.getName());

        try {
            String formattedName = teamData.getName().toLowerCase().replaceAll("[ ']", "_");
            String badgeFilename = teamType + "/" + teamType + "_" + formattedName + ".png";
            InputStream badgeInputStream = getContext().getAssets().open(badgeFilename);
            teamBadge.setImageBitmap(BitmapFactory.decodeStream(badgeInputStream));
            badgeInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        teamNameLabel.setText(teamData.getName());
        teamAttackLabel.setText(String.format("%s %s", getContext().getString(R.string.attackLabel), teamData.getAttack()));
        teamMidfieldLabel.setText(String.format("%s %s", getContext().getString(R.string.midfieldLabel), teamData.getMidfield()));
        teamDefenseLabel.setText(String.format("%s %s", getContext().getString(R.string.defenseLabel), teamData.getDefense()));
        if ("N/A".equals(teamData.getLeague())) {
            teamLeagueLabel.setVisibility(View.INVISIBLE);
        } else {
            teamLeagueLabel.setVisibility(View.VISIBLE);
            teamLeagueLabel.setText(String.format("%s", teamData.getLeague()));
        }
        if ("N/A".equals(teamData.getCountry())) {
            teamCountryLabel.setVisibility(View.INVISIBLE);
        } else {
            teamCountryLabel.setVisibility(View.VISIBLE);
            teamCountryLabel.setText(String.format("%s", teamData.getCountry()));
        }
    }
}
