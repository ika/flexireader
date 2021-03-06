package org.armstrong.ika.FlexiReader.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.armstrong.ika.FlexiReader.app.DividerLineDecoration;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.RecyclerTouchListener;
import org.armstrong.ika.FlexiReader.about.AboutActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MoreFragment extends Fragment {

    private static MoreFragment instance;

    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private MoreFragmentAdapter moreFragmentAdapter;

    private List<MoreModel> moreItems = new ArrayList<MoreModel>();

    private MoreModel moreModel;

    private int currentBackgroundColor;

    private ColorPickerView.WHEEL_TYPE wheelStyle;

    private String ws;

    private String textSize;

    public static MoreFragment newInstance() {

        MoreFragment moreFragment = new MoreFragment();

        return moreFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        textSize = sharedPreferences.getString("textSize", "16");

        currentBackgroundColor = Integer.parseInt(sharedPreferences.getString("color", Integer.toString(R.color.colorPrimaryDark)));

        String items[] = {
                getString(R.string.color_picker),
                getString(R.string.color_picker_style),
                getString(R.string.images),
                getString(R.string.text_size),
                getString(R.string.share),
                getString(R.string.about)
        };

        for (String i : items) {
            moreModel = new MoreModel();
            moreModel.setMoreText(i);
            moreItems.add(moreModel);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.more_fragment, parent, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.MoreRecyclerView);

        moreFragmentAdapter = new MoreFragmentAdapter(moreItems, textSize);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerLineDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(moreFragmentAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            // short click
            public void onClick(View view, int position) {

                switch (position) {

                    case 0: // color picker

                        ws = sharedPreferences.getString("wheelStyle", "0");

                        switch (ws) {
                            case "0":
                                wheelStyle = ColorPickerView.WHEEL_TYPE.FLOWER;
                                break;
                            case "1":
                                wheelStyle = ColorPickerView.WHEEL_TYPE.CIRCLE;
                                break;
                        }

                        ColorPickerDialogBuilder
                                .with(getContext(), R.style.Theme_AppCompat_Dialog_Alert)
                                //.setTitle(R.string.color_dialog_title)
                                .initialColor(currentBackgroundColor)
                                .wheelType(wheelStyle)
                                .density(12)
                                .setOnColorChangedListener(new OnColorChangedListener() {
                                    @Override
                                    public void onColorChanged(int selectedColor) {
                                        // Handle on color change
                                        //toast("onColorChanged: 0x" + Integer.toHexString(selectedColor));
                                    }
                                })
                                .setOnColorSelectedListener(new OnColorSelectedListener() {
                                    @Override
                                    public void onColorSelected(int selectedColor) {
                                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                        //Utils.makeToast(getContext(),"onColorSelected: 0x" + Integer.toHexString(selectedColor));
                                    }
                                })
                                .setPositiveButton(getString(R.string.okay), new ColorPickerClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {

                                        changeCurrentBackgroundColor(selectedColor);

                                    }
                                })
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .showColorEdit(false)
                                //.setColorEditTextColor(ContextCompat.getColor(SampleActivity.this, android.R.color.holo_blue_bright))
                                .build()
                                .show();
                        break;

                    case 1: // Color Picker Style

                        final MoreColorPickerSheet moreColorPickerSheet = new MoreColorPickerSheet();
                        moreColorPickerSheet.show(getActivity().getSupportFragmentManager(), "moreColorPickerSheet");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                moreColorPickerSheet.dismiss();
                            }
                        }, 5000);

                        break;

                    case 2: //Images On/Off

                        final MoreImageSheet moreImageSheet = new MoreImageSheet();
                        moreImageSheet.show(getActivity().getSupportFragmentManager(), "moreImageSheet");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                moreImageSheet.dismiss();
                            }
                        }, 5000);

                        break;

                    case 3: // text size
                        final MoreSizeSheet moreSizeSheet = new MoreSizeSheet();
                        moreSizeSheet.show(getActivity().getSupportFragmentManager(), "moreSizeSheet");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                moreSizeSheet.dismiss();
                            }
                        }, 5000);

                        break;

                    case 4: // share FlexiReader

                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(getString(R.string.shareFRtext));
                        stringBuilder.append("\n\n");
                        stringBuilder.append(getString(R.string.shareFRlink));

                        Intent shareIntent = new Intent();

                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.setType("text/plain");
                        //shareIntent.putExtra(Intent.EXTRA_SUBJECT, subj);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());

                        Intent intent = Intent.createChooser(shareIntent, getString(R.string.share_with));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;

                    case 5: // about
                        Intent moreActivity = new Intent(getContext(), AboutActivity.class);
                        startActivity(moreActivity);
                        break;
                }

            }

            // long click
            public void onLongClick(View view, int position) {
                // do nothing
            }
        }));

    }

    public static MoreFragment getInstance() {
        return instance;
    }

    private void changeCurrentBackgroundColor(int selectedColor) {
        // set local variable
        currentBackgroundColor = selectedColor;
        // set Activity colors
        MoreActivity.getInstance().setBackgroundColors(selectedColor);

    }

    public void updateRecyclerView() {

        textSize = sharedPreferences.getString("textSize", "16");
        moreFragmentAdapter = new MoreFragmentAdapter(moreItems, textSize);
        recyclerView.setAdapter(moreFragmentAdapter);

    }

}
