package org.armstrong.ika.FlexiReader.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.feeds.FeedActivity;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;

import java.util.UUID;

public class AddRecordFragment extends Fragment implements View.OnClickListener {

    private EditText nameEditText;
    private EditText linkEditText;
    private Button button;
    private View view;
    protected FeedsDatabase feedsDatabase;

    public static AddRecordFragment newInstance() {

        AddRecordFragment addRecordFragment = new AddRecordFragment();

        return addRecordFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedsDatabase = FeedsDatabase.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.feed_add_fragment, container, false);

        nameEditText = view.findViewById(R.id.name_edittext);
        linkEditText = view.findViewById(R.id.link_edittext);
        button = view.findViewById(R.id.add_record);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.add_record:

                final String title = nameEditText.getText().toString();
                final String link = linkEditText.getText().toString();

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

                    String feedID = UUID.randomUUID().toString();

                    FeedsEntities feedsEntities = new FeedsEntities();
                    feedsEntities.setTitle(title);
                    feedsEntities.setLink(link);
                    feedsEntities.setFeedId(feedID);

                    feedsDatabase.feedsDoa().insertFeed(feedsEntities);

                    this.returnToFeeds();
                }

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
        Intent intent = new Intent(getActivity(), FeedActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
