package org.armstrong.ika.FlexiReader;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

public class AssetUtils {

    public String LoadAsset(Context context, String inputFile) {

        String asset;

        try {
            InputStream stream = context.getAssets().open(inputFile);

            int size = stream.available();

            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();

            asset = new String(buffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return asset;

    }

}