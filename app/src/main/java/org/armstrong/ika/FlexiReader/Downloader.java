package org.armstrong.ika.FlexiReader;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.armstrong.ika.FlexiReader.MainActivity.swipeRefreshLayout;

public class Downloader extends AsyncTask<Void, Void, Object> {

    private final Context context;
    private final String urlAddress;
    private final String feedID;

    // --Commented out by Inspection (8/3/18 6:42 PM):ProgressDialog pd;

    public Downloader(Context context, String urlAddress, String feedID ) {
        this.context = context;
        this.urlAddress = urlAddress;
        this.feedID = feedID;
    }

    @Override
    protected Object doInBackground(Void... params) {

        return this.downloadData();
    }

    @Override
    protected void onPostExecute(Object data) {

        super.onPostExecute(data);

        if (data == "error") {
            swipeRefreshLayout.setRefreshing(false);
            showToast("Connection Error");
        } else {
            new RSSParser(context, (InputStream) data, feedID ).execute();
        }

    }

    private Object downloadData() {

        Object connection = Connector.connect(urlAddress);

        if (connection == "error") {
            return "error";
        }

        try {
            HttpURLConnection con = (HttpURLConnection) connection;

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return new BufferedInputStream(con.getInputStream());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    protected void showToast(String text) {
        Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
}
