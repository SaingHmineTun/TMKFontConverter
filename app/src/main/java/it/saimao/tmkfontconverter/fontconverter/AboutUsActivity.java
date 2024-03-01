package it.saimao.tmkfontconverter.fontconverter;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import it.saimao.tmkfontconverter.R;

public class AboutUsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private final String[] stringsAsk = {"E-mail:", "Facebook:", "Github:", "Rate this app on Play Store"};
    private final String[] stringsValue = {"tmk.muse@gmail.com", "ထုင့်မၢဝ်းၶမ်း", "Get source code", ""};
    private final int[] icons = {R.drawable.ic_gmail, R.drawable.ic_facebook, R.drawable.github, R.drawable.ic_playstore};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ListView simpleListView = findViewById(R.id.simpleListView);
        MaoAdapter adapter = new MaoAdapter(getBaseContext(), stringsAsk, stringsValue, icons);
        simpleListView.setOnItemClickListener(this);
        simpleListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent;
        if (i == 1) {
            try {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/100377671433172"));
            } catch (Exception e) {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/100377671433172"));
            }
            startActivity(intent);
        } else if (i == 0) {
            String to = "tmk.muse@gmail.com";
            intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        } else if (i == 2) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/SaingHmineTun/TMKFontConverter")));
        } else if (i == 3) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=it.saimao.tmkfontconverter")));
        }
    }
}
