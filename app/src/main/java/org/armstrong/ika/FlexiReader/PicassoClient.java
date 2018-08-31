package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {

    public static void downloadImage(Context context, String imageUrl, ImageView imageView) {

        // adjust image height
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

//        ""("LOGGING", "downloadImage: " + imageUrl.length());

        if (imageUrl != null && imageUrl.length() > 0) {
            layoutParams.height = 230;
            Picasso.with(context).load(imageUrl).placeholder(R.drawable.ic_launcher_background).into(imageView);
        } else {
            layoutParams.height = 0;
            imageView.setImageDrawable(null);
            //Picasso.with(context).load(R.drawable.ic_launcher_background).into(imageView);
        }

        imageView.setLayoutParams(layoutParams);
    }
}
