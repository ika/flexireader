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

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsRepository;
import org.armstrong.ika.FlexiReader.list.ListActivity;

public class ModifyRecordFragment extends Fragment implements View.OnClickListener {

    private EditText nameEditText;
    private EditText linkEditText;
    private View view;
    private Button updateBtn, deleteBtn;

    protected FeedsRepository feedsRepository;

    private String id, title, link;

    public static ModifyRecordFragment newInstance() {

        ModifyRecordFragment modifyRecordFragment = new ModifyRecordFragment();

        return modifyRecordFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedsRepository = new FeedsRepository(getActivity());

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

                long seconds = System.currentTimeMillis() / 1000l;

                String title = nameEditText.getText().toString();
                String link = linkEditText.getText().toString();

                if (title == null || title.isEmpty()) {
                    showError(getString(R.string.feeds_add_name));
                } else if (link == null || link.isEmpty()) {
                    showError(getString(R.string.feeds_add_link));
                } else if (!URLUtil.isValidUrl(link)) {
                    showError(getString(R.string.feeds_add_link_error));
                } else {

                    feedsRepository.updateRecord(title, link, Integer.parseInt(id), seconds);

                    returnToFeeds();

                }

                break;

            case R.id.btn_delete:

                new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.sure))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                feedsRepository.deleteRecord(Integer.parseInt(id));

                                returnToFeeds();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
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
                .setTitle(getString(R.string.error))
                .setMessage(txt)
                .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .create()
                .show();
    }

    private void returnToFeeds() {
        Intent home_intent = new Intent(getActivity(), ListActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
    }


}
