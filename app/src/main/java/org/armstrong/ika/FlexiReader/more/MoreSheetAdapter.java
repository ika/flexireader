package org.armstrong.ika.FlexiReader.more;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MoreSheetAdapter extends RecyclerView.Adapter<MoreSheetAdapter.CustomViewHolder> {

        private List<MoreModel> menuList;

        public MoreSheetAdapter(List<MoreModel> menuList) {
            this.menuList = menuList;

        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.more_sheet_item, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder((view));

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

            final MoreModel moreModel = menuList.get(position);

            int a = Integer.parseInt(moreModel.getMoreActive());
            boolean status = (a == 1) ? true : false;

            customViewHolder.menuItem.setText(moreModel.getMoreText());
            customViewHolder.itemSwitch.setChecked(status);

        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {

            protected TextView menuItem;
            protected Switch itemSwitch;

            public CustomViewHolder(final View view) {
                super(view);

                this.menuItem = view.findViewById(R.id.menuItem);
                this.itemSwitch = view.findViewById(R.id.itemSwitch);

            }

        }

        @Override
        public int getItemCount() {
            return menuList.size();
        }


    }
