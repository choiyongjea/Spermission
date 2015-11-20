package com.example.jay.spermission;

import android.os.Bundle;
import android.preference.PreferenceActivity;
/**
 * Created by silogood on 2015-11-16.
 */
public class Preference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cr�ation de l'interface de pr�f�rences
        addPreferencesFromResource(R.xml.preference);

    }
}
