package com.example.edward.dwarkawala;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import Fragments.FeedsFragment;

public class FullFeed extends AppCompatActivity {
    TextView title;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_feed);

        //title = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.postwebview);
        Intent i = getIntent();
        int position = i.getExtras().getInt("item_position");

        //  title.setText( MainActivity.mListPost.get(position).getTitle().getRendered());
        Log.e("WpPostDetails ", "  title is " + FeedsFragment.postList.get(position).getTitle().getRendered());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(FeedsFragment.postList.get(position).getLink());
        // to open webview inside app -- otherwise It will open url in device browser
        webView.setWebViewClient(new WebViewClient());
    }
}
