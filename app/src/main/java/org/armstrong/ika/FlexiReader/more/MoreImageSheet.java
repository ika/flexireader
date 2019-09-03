package org.armstrong.ika.FlexiReader.more;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreImageSheet extends BottomSheetDialogFragment {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private MoreModel moreModel;
    private List<MoreModel> menuItems = new ArrayList<>();

    private RecyclerView recyclerView;
    private MoreSheetAdapter moreSheetAdapter;

    private Switch aSwitch;

    private View view;

    private String prefImages;

    private String textSize;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefImages = sharedPreferences.getString("prefImages", "1");
        textSize = sharedPreferences.getString("textSize", "16");

        String items[] = {getString(R.string.images)};

        for (String i : items) {
            moreModel = new MoreModel();
            moreModel.setMoreText(i);
            moreModel.setMoreActive(prefImages);
            menuItems.add(moreModel);
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        view = LayoutInflater.from(getContext()).inflate(R.layout.more_imagesheet_fragment, null);

        dialog.setContentView(view);

        ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);

        recyclerView = view.findViewById(R.id.moreImageSheetRecyclerView);

        moreSheetAdapter = new MoreSheetAdapter(menuItems, textSize);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //recyclerView.addItemDecoration(new DividerLineDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(moreSheetAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            // short click
            public void onClick(View view, int position) {

                aSwitch = view.findViewById(R.id.itemSwitch);

                aSwitch.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {

                        String switched = (aSwitch.isChecked()) ? "1" : "0";

                        // make image setting current
                        sharedPreferencesEditor = sharedPreferences.edit();
                        sharedPreferencesEditor.putString("prefImages", switched);
                        sharedPreferencesEditor.commit();

                    }

                });

            }

            // long click
            public void onLongClick(View view, int position) {
                // do nothing

            }


        }));


    }



}
