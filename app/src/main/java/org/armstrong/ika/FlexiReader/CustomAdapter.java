package org.armstrong.ika.FlexiReader;

// https://www.androidhive.info/2016/01/android-working-with-recycler-view/
// https://www.stacktips.com/tutorials/android/android-recyclerview-example

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static org.armstrong.ika.FlexiReader.R.*;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;
    private List<Article> articles;

    public CustomAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout.activity_model, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder((view));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

        Article article = articles.get(position);

        //Render image using Picasso library
        PicassoClient.downloadImage(context, article.getImageUrl(), customViewHolder.postImg);

        customViewHolder.postTitle.setText(article.getTitle());
        customViewHolder.postDesc.setText(article.getDescription());
        customViewHolder.postDate.setText(article.getpubDate());

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }


}
