package org.armstrong.ika.FlexiReader.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.armstrong.ika.FlexiReader.R;
import org.armstrong.ika.FlexiReader.feedsdb.FeedsEntities;

import java.util.List;

public class ListFragmentAdapter extends RecyclerView.Adapter<ListFragmentAdapter.CustomViewHolder> {

    private List<FeedsEntities> listItems;
    private int textSize;

    public ListFragmentAdapter(List<FeedsEntities> listItems, String textSize) {
        this.listItems = listItems;
        this.textSize = Integer.parseInt(textSize);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder((view));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

        final FeedsEntities feedsEntities = listItems.get(position);

        customViewHolder.listItem.setText(feedsEntities.getTitle());
        customViewHolder.listItem.setTextSize(textSize);

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView listItem;

        public CustomViewHolder(final View view) {
            super(view);

            this.listItem = view.findViewById(R.id.listItem);

        }

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


}
