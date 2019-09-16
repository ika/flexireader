package org.armstrong.ika.FlexiReader.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.app.Downloader;
import org.armstrong.ika.FlexiReader.app.RecyclerTouchListener;
import org.armstrong.ika.FlexiReader.cachedb.CacheDatabase;
import org.armstrong.ika.FlexiReader.cachedb.CacheEntities;
import org.armstrong.ika.FlexiReader.app.Utils;
import org.armstrong.ika.FlexiReader.cachedb.CacheRepository;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainFragment extends Fragment {

    private static MainFragment instance;

    private View view;

    private String feedID;
    private String urlAddress;

    private List<CacheEntities> articles;
    private RecyclerView recyclerView;
    private MainFragmentAdapter mainFragmentAdapter;

    public SwipeRefreshLayout swipeRefreshLayout;

    protected CacheRepository cacheRepository;

    private int dbCount;

    private String textSize;

    public static MainFragment newInstance(String feedID, String urlAddress, String textSize) {

        MainFragment mainFragment = new MainFragment();

        Bundle args = new Bundle();
        args.putString("feedID", feedID);
        args.putString("urlAddress", urlAddress);
        args.putString("textSize", textSize);
        mainFragment.setArguments(args);

        return mainFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        feedID = getArguments().getString("feedID", "");
        urlAddress = getArguments().getString("urlAddress", "");
        textSize = getArguments().getString("textSize", "16");

        cacheRepository = new CacheRepository(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_fragment, parent, false);

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        recyclerView = view.findViewById(R.id.recycler_view);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // setup refresh
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {  // on pull down
                        downLoadFeed();
                    }
                });
        swipeRefreshLayout.setDistanceToTriggerSync(30);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // run refresh
        swipeRefreshLayout.post(new Runnable() { // on start up
                                    @Override
                                    public void run() {
                                        if (getDbCount() < 1) {
                                            downLoadFeed();
                                        } else {
                                            displayList();
                                        }
                                    }
                                }
        );

        // click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new RecyclerTouchListener.ClickListener() {

            // go to link
            public void onClick(View view, int position) {

                CacheEntities article = articles.get(position);

                String link = article.getLink();

                if (URLUtil.isValidUrl(link)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link));
                    getActivity().startActivity(intent);
                } else {
                    Utils.makeToast(getContext(),getString(R.string.url_invalid));
                }


            }

            // Share article
            public void onLongClick(View view, int position) {

                CacheEntities article = articles.get(position);
              //  String title = article.getTitle();
                String link = article.getLink();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
              //  sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                getActivity().startActivity(Intent.createChooser(sendIntent,getString(R.string.share_title)));

            }

        }));

    }

    public static MainFragment getInstance() {
        return instance;
    }


    public void displayList() {

        articles = cacheRepository.getAllCacheRecords(feedID);

        mainFragmentAdapter = new MainFragmentAdapter(getContext(), articles, textSize);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(mainFragmentAdapter);

    }

    private int getDbCount() {
        dbCount = cacheRepository.countCacheByFeed(feedID);
        return dbCount;
    }

    private void downLoadFeed() {

        if (!feedID.isEmpty()) {

            if (!urlAddress.isEmpty()) {

                swipeRefreshLayout.setRefreshing(true);

                new Downloader(getActivity(), urlAddress, feedID).execute();

            } else {

                Utils.makeToast(getContext(), getString(R.string.url_empty));
            }
        } else {

            Utils.makeToast(getContext(), getString(R.string.feed_empty));
        }

    }


}
