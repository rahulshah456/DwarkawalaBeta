package com.example.edward.dwarkawala;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import Fragments.FeedsFragment;

public class FullFeed extends AppCompatActivity {

    public static final String TAG = FullFeed.class.getSimpleName();
    private TextView title;
    private WebView webView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_feed);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //if (shouldChangeStatusBarTintToDark) {
//                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                // We want to change tint color to white again.
//                // You can also record the flags in advance so that you can turn UI back completely if
//                // you have set other flags before, such as translucent or full screen.
//                decor.setSystemUiVisibility(0);
//            }
        }

        //title = (TextView) findViewById(R.id.title);
        webView = (WebView) findViewById(R.id.postwebview);
        backButton = (ImageButton) findViewById(R.id.backButtonID);
        Intent i = getIntent();
        int position = i.getExtras().getInt("item_position");

        //  title.setText( MainActivity.mListPost.get(position).getTitle().getRendered());
        Log.e("WpPostDetails ", "  title is " + FeedsFragment.postList.get(position).getTitle().getRendered());

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(FeedsFragment.postList.get(position).getLink());
        // to open webview inside app -- otherwise It will open url in device browser
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByTagName('header')[0];"
                        + "head.parentNode.removeChild(head);" +
                        "})()");

                webView.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByTagName('footer')[0];"
                        + "head.parentNode.removeChild(head);" +
                        "})()");

                webView.loadUrl("javascript:var x = document.getElementsByClassName" +
                        "('responsive-menu-inner')[0].style.display='none';");

                webView.loadUrl("javascript:var x = document.getElementsByClassName" +
                        "('responsive-menu-button')[0].style.display='none';");

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
