package org.armstrong.ika.FlexiReader.feeds;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.armstrong.ika.FlexiReader.app.DividerLineDecoration;
import org.armstrong.ika.FlexiReader.modify.ModifyRecordActivity;
import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.RecyclerTouchListener;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsDatabase;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;
import org.armstrong.ika.FlexiReader.app.Utils;

import java.util.List;

public class FeedFragment extends Fragment {

    private View view;

    protected FeedsDatabase feedsDatabase;

    private RecyclerView recyclerView;

    private FeedFragmentAdapter feedFragmentAdapter;

    List<FeedsEntities> records;

    private String textSize;


    public static FeedFragment newInstance(String textSize) {

        FeedFragment feedFragment = new FeedFragment();

        Bundle args = new Bundle();
        args.putString("textSize", textSize);
        feedFragment.setArguments(args);

        return feedFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedsDatabase = FeedsDatabase.getInstance(getContext());
        textSize = getArguments().getString("textSize", "14");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.feed_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        records = feedsDatabase.feedsDoa().getFeedsRecords();

        recyclerView = view.findViewById(R.id.FeedsRecyclerView);

        feedFragmentAdapter = new FeedFragmentAdapter(records, textSize);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerLineDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(feedFragmentAdapter);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String cnt = Integer.toString(feedsDatabase.feedsDoa().countRecords());
        cnt = cnt + " items";

        Utils.makeToast(getContext(), cnt);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            // short click
            public void onClick(View view, int position) {

                FeedsEntities feedsEntities = records.get(position);

                Intent modifyRecord = new Intent(getActivity(), ModifyRecordActivity.class);
                modifyRecord.putExtra("title", feedsEntities.getTitle());
                modifyRecord.putExtra("link", feedsEntities.getLink());
                modifyRecord.putExtra("id", Integer.toString(feedsEntities.getId()));
                startActivity(modifyRecord);

            }

            // long click
            public void onLongClick(View view, int position) {
                // do nothing
            }

        }));


    }


}
