package com.example.hired;

import static com.example.hired.userSurvey.getPrefs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ProfileActivity extends AppCompatActivity {
    Button filtersBut, videosBut;
    TextView locationText, ageText, fieldPref, skillPref1, skillPref2, skillPref3, nameText;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        filtersBut = (Button) findViewById(R.id.filtersButton);
        videosBut = (Button) findViewById(R.id.videos_button);
        locationText = (TextView) findViewById(R.id.locationLabel);
        fieldPref = (TextView) findViewById(R.id.filter1);
        nameText = (TextView) findViewById(R.id.userName);
        skillPref1 = (TextView) findViewById(R.id.filter);
        skillPref2 = (TextView) findViewById(R.id.filter2);
        skillPref3 = (TextView) findViewById(R.id.filter3);
        ageText = (TextView) findViewById(R.id.ageLabel);
        locationText.setText(getPrefs(this, "locationLabel"));
        ageText.setText(getPrefs(this, "ageLabel"));
        skillPref1.setText(getPrefs(this, "skill1Label"));
        skillPref2.setText(getPrefs(this, "skill2Label"));
        skillPref3.setText(getPrefs(this, "skill3Label"));
        logo = (ImageView) findViewById(R.id.logoHomeButton);
        logo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        videosBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VideoDisplay.class);
                startActivity(intent);
            }});

        filtersBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), userSurvey.class);
                startActivity(intent);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        nameText.setText(getPrefs(this, "nameLabel"));
        locationText.setText(getPrefs(this, "locationLabel"));
        ageText.setText(getPrefs(this, "ageLabel"));
        fieldPref.setText(getPrefs(this, "interestLabel"));
        skillPref1.setText(getPrefs(this, "skill1Label"));
        skillPref2.setText(getPrefs(this, "skill2Label"));
        skillPref3.setText(getPrefs(this, "skill3Label"));
    }

}


