package org.armstrong.ika.FlexiReader.more;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MoreSizeSheetAdapter extends RecyclerView.Adapter<MoreSizeSheetAdapter.CustomViewHolder> {

    private List<MoreSizeSheetModel> menuList;
    private String textSize;
    private int color;

    public MoreSizeSheetAdapter(List<MoreSizeSheetModel> menulist, String textsize, String color) {
        this.menuList = menulist;
        this.textSize = textsize;
        this.color = Integer.parseInt(color);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.moresize_sheet_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder((view));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

        final MoreSizeSheetModel moreSizeSheetModel = menuList.get(position);

        customViewHolder.moreSizeSheetItem.setText(moreSizeSheetModel.getMenuText());
        customViewHolder.moreSizeSheetItem.setTextSize(Integer.parseInt(moreSizeSheetModel.getMenuSize()));

        if (moreSizeSheetModel.getMenuSize().equals(textSize)) {
            customViewHolder.moreSizeSheetItem.setTypeface(null, Typeface.BOLD_ITALIC);
            customViewHolder.moreSizeSheetItem.setTextColor(color);
        }

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView moreSizeSheetItem;

        public CustomViewHolder(final View view) {
            super(view);

            this.moreSizeSheetItem = view.findViewById(R.id.moreSizeSheetItem);

        }

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }


}
