package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class PicassoClient {

    public static void downloadImage(Context context, String imageUrl, ImageView imageView) {

        // adjust image height
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        if (TextUtils.isEmpty(imageUrl)) {
            layoutParams.height = 8;
            imageView.setImageDrawable(null);
        } else {
            layoutParams.height = 250;
            Picasso.with(context).load(imageUrl).error(R.drawable.ic_launcher_background).placeholder(R.drawable.ic_launcher_background).into(imageView);
        }

        imageView.setLayoutParams(layoutParams);
    }
}
