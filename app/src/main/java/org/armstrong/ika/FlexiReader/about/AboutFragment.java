package org.armstrong.ika.FlexiReader.about;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.Utils;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    View view;
    private String color;
    private TextView appName;
    private TextView aboutText;
    public String content;
    private int clickCount = 0;
    private String textSize;

    SharedPreferences sharedPreferences;

    public static AboutFragment newInstance() {

        AboutFragment aboutFragment = new AboutFragment();

        return aboutFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        textSize = sharedPreferences.getString("textSize", "14");

        content = Utils.loadAsset(getContext(),"about.txt");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.about_fragment, parent, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appName = view.findViewById(R.id.appName);
        appName.setTextColor(Integer.parseInt(color));
        appName.setTextSize(Integer.parseInt(textSize) + 4);

        aboutText = view.findViewById(R.id.aboutText);
        aboutText.setText(content);
        aboutText.setTextSize(Integer.parseInt(textSize));

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}
