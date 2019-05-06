package org.armstrong.ika.FlexiReader.feeds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class FeedFragmentAdapter extends RecyclerView.Adapter<FeedFragmentAdapter.CustomViewHolder> {

        private List<FeedsEntities> modelList;
        private String textSize;

        public FeedFragmentAdapter(List<FeedsEntities> modelList, String textSize) {
            this.modelList = modelList;
            this.textSize = textSize;

        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.feed_item, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder((view));

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

            final FeedsEntities feedsEntities = modelList.get(position);

            customViewHolder.feed_title.setText(feedsEntities.getTitle());
            customViewHolder.feed_title.setTextSize(Integer.parseInt(textSize));

        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {

            protected TextView feed_title;

            public CustomViewHolder(final View view) {
                super(view);

                this.feed_title = view.findViewById(R.id.feedItem);

            }

        }

        @Override
        public int getItemCount() {
            return modelList.size();
        }


    }
