package org.armstrong.ika.FlexiReader.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.armstrong.ika.FlexiReader.MainActivity;
import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.DividerLineDecoration;
import org.armstrong.ika.FlexiReader.app.RecyclerTouchListener;
import org.armstrong.ika.FlexiReader.app.Utils;

import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;
import org.armstrong.ika.FlexiReader.modify.ModifyRecordActivity;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor sharedPreferencesEditor;

    protected FeedsDatabase feedsDatabase;

    private RecyclerView recyclerView;
    private ListFragmentAdapter listFragmentAdapter;

    private List<FeedsEntities> listItems = new ArrayList<FeedsEntities>();

    private String textSize;

    String query;

    public static ListFragment newInstance(String query) {

        ListFragment listFragment = new ListFragment();

        Bundle args = new Bundle();
        args.putString("query", query);
        listFragment.setArguments(args);

        return listFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        textSize = sharedPreferences.getString("textSize", "16");

        feedsDatabase = FeedsDatabase.getInstance(getActivity());

        query = getArguments().getString("query", "");

        if (query.equals("")) {
            listItems = feedsDatabase.feedsDoa().getFeedsRecords();
        } else {
            listItems = feedsDatabase.feedsDoa().getFeedSearchRecords(query);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_fragment, parent, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.ListRecyclerView);

        listFragmentAdapter = new ListFragmentAdapter(listItems, textSize);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerLineDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(listFragmentAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String cnt = Integer.toString(feedsDatabase.feedsDoa().countRecords());
        cnt = cnt + " " + getString(R.string.items);

        Utils.makeToast(getContext(), cnt);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            // short click
            public void onClick(View view, int position) {

                long seconds = System.currentTimeMillis() / 1000l;

                FeedsEntities feedsEntities = listItems.get(position);
                int id = feedsEntities.getId();

                sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putInt("dbid", id);
                sharedPreferencesEditor.apply();

                // update time to use in sorting
                feedsDatabase.feedsDoa().updateTime(seconds, id);

                Intent mainActivity = new Intent(getActivity(), MainActivity.class);
                startActivity(mainActivity);

            }

            // long click
            public void onLongClick(View view, int position) {

                FeedsEntities feedsEntities = listItems.get(position);

                Intent modifyRecord = new Intent(getActivity(), ModifyRecordActivity.class);
                modifyRecord.putExtra("title", feedsEntities.getTitle());
                modifyRecord.putExtra("link", feedsEntities.getLink());
                modifyRecord.putExtra("id", Integer.toString(feedsEntities.getId()));
                startActivity(modifyRecord);
            }
        }));

    }


}
