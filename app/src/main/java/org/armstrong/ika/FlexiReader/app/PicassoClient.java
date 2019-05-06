package org.armstrong.ika.FlexiReader.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.armstrong.ika.FlexiReader.R;

import androidx.appcompat.app.AppCompatDelegate;

public class PicassoClient {

    private static SharedPreferences sharedPreferences;

    private static String prefImages;

    public static void downloadImage(Context context, String imageUrl, ImageView imageView) {

        int noPicture = 0;
        int withPicture = 250;

        // get user preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        prefImages = sharedPreferences.getString("prefImages", "1");

        // adjust image height
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();

        if (prefImages.equals("1")) { // image setting

            final StateListDrawable placeholderList = (StateListDrawable) context.getResources().getDrawable(R.drawable.state_list_placeholder);

            if (TextUtils.isEmpty(imageUrl) || !URLUtil.isValidUrl(imageUrl)) {

                layoutParams.height = noPicture;
                imageView.setImageDrawable(null);

            } else {

                layoutParams.height = withPicture;

                Picasso.with(context).load(imageUrl)
                        .error(placeholderList.getCurrent())
                        .placeholder(placeholderList.getCurrent())
                        .into(imageView);

            }

        } else {

            layoutParams.height = noPicture;
            imageView.setImageDrawable(null);
        }

        imageView.setLayoutParams(layoutParams);
    }


}
