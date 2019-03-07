package com.simarro.practicas.domotiapp.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;


import com.simarro.practicas.domotiapp.R;


public class FragmentSetup extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.content, new PreferenciasFragment());
        ft.commit();
    }
    public static class PreferenciasFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setup_fragment);
        }
    }
}
