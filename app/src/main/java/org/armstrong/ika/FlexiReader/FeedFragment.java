package org.armstrong.ika.FlexiReader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedFragment extends Fragment {

    private View view;

    private final String[] from = new String[]{
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_TITLE,
            DatabaseHelper.COLUMN_LINK};

    private final int[] to = new int[]{R.id.id, R.id.title, R.id.link};

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_records_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DBManager dbManager = new DBManager(getActivity());

        Cursor cursor = dbManager.getCursorRecords();

        ListView listView = view.findViewById(R.id.list_view);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        String cnt = Integer.toString(dbManager.countRecords());
        cnt = cnt + " items";
        Toast.makeText(getActivity(), cnt, Toast.LENGTH_SHORT).show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {

                TextView idTextView = view.findViewById(R.id.id);
                TextView titleTextView = view.findViewById(R.id.title);
                TextView linkTextView = view.findViewById(R.id.link);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String link = linkTextView.getText().toString();

                Intent modify_intent = new Intent(getActivity(), ModifyRecordActivity.class);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("link", link);
                modify_intent.putExtra("id", id);
                startActivity(modify_intent);
            }
        });


    }


}
