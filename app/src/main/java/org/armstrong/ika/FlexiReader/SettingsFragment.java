package org.armstrong.ika.FlexiReader;

// https://www.androidhive.info/2017/07/android-implementing-preferences-settings-screen/

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        bindPreferenceSummaryToValue(findPreference("prefImagesSettings"));
        bindPreferenceSummaryToValue(findPreference("prefCacheSetting"));
//        bindPreferenceSummaryToValue(findPreference("prefCacheTotal"));

    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        if (preference instanceof CheckBoxPreference) {

            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getBoolean(preference.getKey(), false));

        } else if (preference instanceof ListPreference) {

            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    private static final Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index] + " hours"
                                : null);

            } else if (preference instanceof CheckBoxPreference) {

                if (stringValue.equals("true")) {
                    preference.setSummary(R.string.pref_image_summary_on);
                } else {
                    preference.setSummary(R.string.pref_image_summary_off);
                }

            } else {
                preference.setSummary(stringValue);
            }

            return true;
        }
    };


}

