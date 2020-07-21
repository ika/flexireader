package org.armstrong.ika.FlexiReader.main;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.armstrong.ika.FlexiReader.app.PicassoClient;
import org.armstrong.ika.FlexiReader.cachedb.CacheEntities;

import java.io.IOException;
import java.util.List;

import static org.armstrong.ika.FlexiReader.R.*;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.CustomViewHolder> {

    private Context context;
    private List<CacheEntities> articles;
    private int textSize;

    public MainFragmentAdapter(Context context, List<CacheEntities> articles, String textSize) {
        this.articles = articles;
        this.context = context;
        this.textSize = Integer.parseInt(textSize);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout.main_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder((view));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

        CacheEntities article = articles.get(position);

        //Render image using Picasso library
        try {
            PicassoClient.downloadImage(context, article.getImageUrl(), customViewHolder.postImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        customViewHolder.postTitle.setText(Html.fromHtml(article.getTitle()));
        customViewHolder.postTitle.setTextSize(textSize + 4);

        customViewHolder.postDesc.setText(Html.fromHtml(article.getDescription()));
        customViewHolder.postDesc.setTextSize(textSize);

        customViewHolder.postDate.setText(article.getPubDate());
        customViewHolder.postDate.setTextSize(textSize - 4);

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        //protected ImageButton imageButton;
        protected ImageView postImg;
        protected TextView postTitle;
        protected TextView postDesc;
        protected TextView postDate;

        public CustomViewHolder(View view) {
            super(view);

            this.postTitle = view.findViewById(id.titleTxt);
            this.postDesc = view.findViewById(id.descTxt);
            this.postDate = view.findViewById(id.dateTxt);
            this.postImg = view.findViewById(id.articleImage);
            //this.imageButton = view.findViewById(id.imageButton);


        }


    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


}
