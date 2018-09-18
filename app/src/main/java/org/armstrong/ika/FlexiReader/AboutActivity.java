package org.armstrong.ika.FlexiReader;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.onBackPressed();
                break;

            case R.id.menu_about:

                String CONTENT = new AssetUtils().LoadAsset(this,"about.txt");

                final Dialog dialog;
                dialog = new Dialog(this);
                dialog.setContentView(R.layout.activity_about);
                dialog.setTitle(R.string.action_about);

                TextView text = dialog.findViewById(R.id.dialogText);
                text.setText(CONTENT);

                Button dialogButton = dialog.findViewById(R.id.dialogButton);

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;

            default:
                break;
        }

        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
