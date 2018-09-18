package org.armstrong.ika.FlexiReader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyRecordFragment extends Fragment implements View.OnClickListener {

    private TextView hiddenID;
    private EditText nameEditText;
    private EditText linkEditText;
    private View view;
    private Button updateBtn, deleteBtn;
    private DBManager dbManager;

    public static final String PREFS_NAME = "FlexiReader";
    public static final String PREFS_KEY = "dbid";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_modify_record_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbManager = new DBManager(getActivity());

        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String link = intent.getStringExtra("link");

        hiddenID = view.findViewById(R.id.hiddenID);
        nameEditText = view.findViewById(R.id.name_edittext);
        linkEditText = view.findViewById(R.id.link_edittext);

        hiddenID.setText(id);
        nameEditText.setText(title);
        linkEditText.setText(link);

        updateBtn = view.findViewById(R.id.btn_update);
        deleteBtn = view.findViewById(R.id.btn_delete);

        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_update:

                String id = hiddenID.getText().toString();
                String title = nameEditText.getText().toString();
                String link = linkEditText.getText().toString();

                if (title == null || title.isEmpty()) {
                    String text = getString(R.string.feeds_add_name);
                    showToast(text);
                } else if (link == null || link.isEmpty()) {
                    String text = getString(R.string.feeds_add_link);
                    showToast(text);
                } else if (!link.startsWith("http")) {
                    String text = getString(R.string.feeds_add_link_error);
                    showToast(text);
                } else {

                    ValuesModel values = new ValuesModel();
                    values.setId(id);
                    values.setTitle(title);
                    values.setLink(link);

                    dbManager.updateRecord(values);

                }

                break;

            case R.id.btn_delete:

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete")
                        .setMessage("Do you want to delete this feed?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String did = hiddenID.getText().toString();

                                ValuesModel value = new ValuesModel();
                                value.setId(did);

                                dbManager.deleteRecord(value);

                                returnToFeeds();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                returnToFeeds();
                            }
                        })
                        .create()
                        .show();

                break;
        }


    }

    private void returnToFeeds() {
        Intent home_intent = new Intent(getActivity(), FeedActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }


}
