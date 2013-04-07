package com.spanisharmada.droidcon.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.spanisharmada.droidcon.R;
import com.spanisharmada.droidcon.ui.fragments.SAScanListFragment;

public class SAMainFragmentActivity extends FragmentActivity {

    private Fragment mScanListFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment_activity);

        mScanListFragment = new SAScanListFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.SA_fragment_place, mScanListFragment);
        transaction.commit();
    }

}
