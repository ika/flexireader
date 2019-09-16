package org.armstrong.ika.FlexiReader.more;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MoreColorPickerSheetAdapter extends RecyclerView.Adapter<MoreColorPickerSheetAdapter.CustomViewHolder> {

        private List<MoreColorPickerModel> menuList;
        private String ws;
        private int color;
    private int textSize;

        public MoreColorPickerSheetAdapter(List<MoreColorPickerModel> menulist,
                                           String ws, String color, String textSize) {
            this.menuList = menulist;
            this.ws = ws;
            this.color = Integer.parseInt(color);
            this.textSize = Integer.parseInt(textSize);

        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.more_colorpicker_sheet_item, viewGroup, false);

            CustomViewHolder viewHolder = new CustomViewHolder((view));

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

            final MoreColorPickerModel moreColorPickerModel = menuList.get(position);

            customViewHolder.colorPickerSheetItem.setText(moreColorPickerModel.getText());

            if (moreColorPickerModel.getNumber().equals(ws)) {
                customViewHolder.colorPickerSheetItem.setTextColor(color);
                customViewHolder.colorPickerSheetItem.setTypeface(null, Typeface.BOLD_ITALIC);
            }

            customViewHolder.colorPickerSheetItem.setTextSize(textSize);

        }

        public class CustomViewHolder extends RecyclerView.ViewHolder {

            protected TextView colorPickerSheetItem;

            public CustomViewHolder(final View view) {
                super(view);

                this.colorPickerSheetItem = view.findViewById(R.id.colorPickerSheetItem);

            }

        }

        @Override
        public int getItemCount() {
            return menuList.size();
        }


    }
