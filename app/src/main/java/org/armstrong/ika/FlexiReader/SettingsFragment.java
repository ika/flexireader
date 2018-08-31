package org.armstrong.ika.FlexiReader;

// https://www.androidhive.info/2017/07/android-implementing-preferences-settings-screen/

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        bindPreferenceSummaryToValue(findPreference("prefImages"));

    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        if (preference instanceof CheckBoxPreference) {

            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getBoolean(preference.getKey(), false));
        }
    }

    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener;

    static {
        sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {

                String stringValue = value.toString();

                if (stringValue.equals("true")) {
                    preference.setSummary(R.string.pref_image_summary_on);
                } else {
                    preference.setSummary(R.string.pref_image_summary_off);
                }

                return true;
            }
        };
    }


}

