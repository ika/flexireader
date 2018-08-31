package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Article> articles;

    public CustomAdapter(Context c, ArrayList<Article> articles) {
        this.c = c;
        this.articles = articles;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.activity_model, parent, false);
        }

        TextView titleTxt = convertView.findViewById(R.id.titleTxt);
        TextView descTxt = convertView.findViewById(R.id.descTxt);
        TextView dateTxt = convertView.findViewById(R.id.dateTxt);
        ImageView img = convertView.findViewById(R.id.articleImage);

        Article article = (Article) this.getItem(position);

        String title = article.getTitle();
        String desc = article.getDescription();
        String date = article.getpubDate();
        String imageUrl = article.getImageUrl();

        final String link = article.getLink();

        titleTxt.setText(title);
        descTxt.setText(desc);
        dateTxt.setText(date);

        PicassoClient.downloadImage(c, imageUrl, img);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(c, link, Toast.LENGTH_SHORT).show();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                c.startActivity(browserIntent);
            }
        });

        return convertView;
    }
}
