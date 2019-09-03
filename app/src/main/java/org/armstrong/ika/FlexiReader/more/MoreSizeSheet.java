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

public class MoreSizeSheet extends BottomSheetDialogFragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    private RecyclerView recyclerView;
    private MoreSizeSheetAdapter moreSizeSheetAdapter;

    private List<MoreSizeSheetModel> menuItems = new ArrayList<>();
    MoreSizeSheetModel moreSizeSheetModel;

    private String textSize;

    private String color;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        color = sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark));
        textSize = sharedPreferences.getString("textSize", "16");

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        String size[] = {"16", "18", "20", "22"};

        String items[] = {
                getString(R.string.small),
                getString(R.string.medium),
                getString(R.string.large),
                getString(R.string.xlarge)
        };

        int x = 0;
        for (String i : items) {
            moreSizeSheetModel = new MoreSizeSheetModel();
            moreSizeSheetModel.setMenuText(i);
            moreSizeSheetModel.setMenuSize(size[x]);
            menuItems.add(moreSizeSheetModel);
            x++;
        }


        //Set the custom view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.more_size_sheet_fragment, null);

        dialog.setContentView(view);

        ((View) view.getParent()).setBackgroundColor(Color.TRANSPARENT);

        recyclerView = view.findViewById(R.id.MoreSizeSheetRecyclerView);

        moreSizeSheetAdapter = new MoreSizeSheetAdapter(menuItems, textSize, color);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerLineDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(moreSizeSheetAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            public void onClick(View view, int position) {

                dismiss();

                sharedPreferencesEditor = sharedPreferences.edit();

                moreSizeSheetModel = menuItems.get(position);

                switch (position) {
                    case 0: // small

                        sharedPreferencesEditor.putString("textSize", moreSizeSheetModel.getMenuSize());

                        break;
                    case 1: // medium

                        sharedPreferencesEditor.putString("textSize", moreSizeSheetModel.getMenuSize());

                        break;

                    case 2: // large

                        sharedPreferencesEditor.putString("textSize", moreSizeSheetModel.getMenuSize());

                        break;

                    case 3: // xlarge

                        sharedPreferencesEditor.putString("textSize", moreSizeSheetModel.getMenuSize());

                        break;

                }

                sharedPreferencesEditor.commit();

                MoreFragment.getInstance().updateRecyclerView();

            }

            public void onLongClick(View view, int position) {
                //do nothing
            }
        }));

    }

}
