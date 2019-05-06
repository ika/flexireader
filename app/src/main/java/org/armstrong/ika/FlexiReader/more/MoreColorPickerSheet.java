package org.armstrong.ika.FlexiReader.more;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.armstrong.ika.FlexiReader.app.DividerLineDecoration;
import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreColorPickerSheet extends BottomSheetDialogFragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    private RecyclerView recyclerView;
    private MoreColorPickerSheetAdapter moreColorPickerSheetAdapter;

    private List<MoreColorPickerModel> menuItems = new ArrayList<>();
    private MoreColorPickerModel moreColorPickerModel;
    private String ws;
    private String color;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ws = sharedPreferences.getString("wheelStyle", "0");
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        String number[] = {"0","1"};
        String items[] = {"Flower", "Circle"};

        int x = 0;
        for (String i : items) {
            moreColorPickerModel = new MoreColorPickerModel();
            moreColorPickerModel.setText(i);
            moreColorPickerModel.setNumber(number[x]);
            menuItems.add(moreColorPickerModel);
            x++;
        }

        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.notes_sheet_fragment, null);

        dialog.setContentView(view);

        ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);

        recyclerView = view.findViewById(R.id.NotesSheetRecyclerView);

        moreColorPickerSheetAdapter = new MoreColorPickerSheetAdapter(menuItems, ws, color);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerLineDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(moreColorPickerSheetAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            public void onClick(View view, int position) {

                String ws = "0";

                switch (position) {
                    case 0: // flower
                        ws = "0";
                        break;
                    case 1: // circle
                        ws = "1";
                        break;

                }

                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString("wheelStyle", ws);
                sharedPreferencesEditor.commit();

                dismiss();

            }

            public void onLongClick(View view, int position) {
                //do nothing
            }
        }));



    }



}
