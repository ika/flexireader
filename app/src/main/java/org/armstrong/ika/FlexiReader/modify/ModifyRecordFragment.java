package org.armstrong.ika.FlexiReader.modify;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.feeds.FeedActivity;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;

public class ModifyRecordFragment extends Fragment implements View.OnClickListener {

    private TextView hiddenID;
    private EditText nameEditText;
    private EditText linkEditText;
    private View view;
    private Button updateBtn, deleteBtn;

    protected FeedsDatabase feedsDatabase;

    private String id, title, link;

    public static ModifyRecordFragment newInstance() {

        ModifyRecordFragment modifyRecordFragment = new ModifyRecordFragment();

        return modifyRecordFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedsDatabase = FeedsDatabase.getInstance(getContext());

        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        link = intent.getStringExtra("link");


    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //view = inflater.inflate(R.layout.feed_modify_fragment, container, false);
        view = inflater.inflate(R.layout.feed_edit_fragment, container, false);

        nameEditText = view.findViewById(R.id.name_edittext);
        linkEditText = view.findViewById(R.id.link_edittext);

        updateBtn = view.findViewById(R.id.btn_update);
        deleteBtn = view.findViewById(R.id.btn_delete);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameEditText.setText(title);
        linkEditText.setText(link);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_update:

                //String id = hiddenID.getText().toString();
                String title = nameEditText.getText().toString();
                String link = linkEditText.getText().toString();

                if (title == null || title.isEmpty()) {
                    String text = getString(R.string.feeds_add_name);
                    showError(text);
                } else if (link == null || link.isEmpty()) {
                    String text = getString(R.string.feeds_add_link);
                    showError(text);
                } else if (!URLUtil.isValidUrl(link)) {
                    String text = getString(R.string.feeds_add_link_error);
                    showError(text);
                } else {

                    feedsDatabase.feedsDoa().updateRecord(title, link, Integer.parseInt(id));

                    returnToFeeds();

                }

                break;

            case R.id.btn_delete:

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                feedsDatabase.feedsDoa().deleteRecord(Integer.parseInt(id));

                                returnToFeeds();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        })
                        .create()
                        .show();

                break;
        }


    }

    private void showError(String txt) {

        new AlertDialog.Builder(getContext())
                .setTitle("Error!")
                .setMessage(txt)
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .create()
                .show();
    }

    private void returnToFeeds() {
        Intent home_intent = new Intent(getActivity(), FeedActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }


}
