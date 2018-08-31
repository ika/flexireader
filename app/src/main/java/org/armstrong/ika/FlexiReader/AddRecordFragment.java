package org.armstrong.ika.FlexiReader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddRecordFragment extends Fragment implements View.OnClickListener {

    private EditText nameEditText;
    private EditText linkEditText;
    private Button button;
    private View view;
    private DBManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_add_record_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dbManager = new DBManager(getActivity());

        nameEditText = view.findViewById(R.id.name_edittext);
        linkEditText = view.findViewById(R.id.link_edittext);
        button = view.findViewById(R.id.add_record);

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
                    showToast(text);
                } else if (link == null || link.isEmpty()) {
                    String text = getString(R.string.feeds_add_link);
                    showToast(text);
                } else if (!link.startsWith("http")) {
                    String text = getString(R.string.feeds_add_link_error);
                    showToast(text);
                } else {

                    ValuesModel value = new ValuesModel();
                    value.setTitle(title);
                    value.setLink(link);

                    dbManager.insertRecord(value);

                    if (dbManager != null) {
                        dbManager.close();
                    }

                    this.returnToFeeds();
                }

                break;
        }

    }

    private void returnToFeeds() {
        Intent intent = new Intent(getActivity(), FeedActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
