package com.spanisharmada.droidcon.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.spanisharmada.droidcon.R;
import com.spanisharmada.droidcon.data.model.SAProfileData;

public class SAProfileDetailsActivity extends FragmentActivity {

    private TextView mName;
    private ImageView mPictureUrl;
    private TextView mSex;
    private TextView mAge;
    private TextView mLocation;
    private TextView mInterests;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile_details);

        mName = (TextView) findViewById(R.id.SA_NameData);
        mPictureUrl = (ImageView) findViewById(R.id.SA_profileImage);
        mSex = (TextView) findViewById(R.id.SA_SexData);
        mAge = (TextView) findViewById(R.id.SA_AgeData);
        mLocation = (TextView) findViewById(R.id.SA_LocationData);
        mInterests = (TextView) findViewById(R.id.SA_InterestsData);

        Intent intent = getIntent();
        if (intent != null) {

            // SAProfileData profileData = (SAProfileData) intent
            // .getParcelableExtra(SAProfileData.PROFILE_DATA_KEY);
            //
            // fillProfileDetails(profileData);

            String profileId = intent
                    .getStringExtra(SAProfileData.PROFILE_DATA_KEY);
            fillProfileDetails(profileId);
        }

    }

    private void fillProfileDetails(final SAProfileData profileData) {

        if (profileData != null) {
            mName.setText(profileData.getName());

            // TODO put the image

            mSex.setText(profileData.getSex());
            mAge.setText(profileData.getAge());
            mLocation.setText(profileData.getLocation());
            mInterests.setText(profileData.getInterests());
        }
    }

    private void fillProfileDetails(final String profileId) {

        String name = "defaultName";
        int pictureUrl = 0;
        String sex = "female";
        String location = "locationData";
        String interests = "interestasData";
        int age = 0;

        // KAtie
        if (profileId.trim().equals("99999")) {
            name = "Katie";
            pictureUrl = R.drawable.katie;
            sex = "female";
            age = 23;
            location = "Berlin";
            interests = "reading-dancing-movies-coding";

        } else if (profileId.trim().equals("11111")) {
            name = "Sonia";
            pictureUrl = R.drawable.sonia;
            sex = "female";
            age = 25;
            location = "London";
            interests = "business-golf-technology-running";
        }

        mName.setText(name);
        mPictureUrl.setImageDrawable(getResources().getDrawable(pictureUrl));
        mSex.setText(sex);
        mAge.setText(String.valueOf(age));
        mLocation.setText(location);
        mInterests.setText(interests);
    }
}
